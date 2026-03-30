package com.itcast.myweb.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.domain.dto.MerchantDTO;
import com.itcast.myweb.domain.dto.UserDTO;
import com.itcast.myweb.utils.Jwt;
import com.itcast.myweb.utils.MerchantHolder;
import com.itcast.myweb.utils.UserHolder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
@Slf4j
public class Interceptor implements HandlerInterceptor {//拦截器


    private StringRedisTemplate stringRedisTemplate;


    //preHandle方法返回true表示继续流程（如调用下一个拦截器或处理器）；返回false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求头中的令牌
        String token = request.getHeader("Authorization");

        //判断令牌是否存在
        if (token == null) {
            // 直接返回未认证响应
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 状态码
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未认证，请先登录\"}");
            return false; // 中断流程
        }

        //令牌解析结果集
        Map<String, Object> map = null;

        try {
            //解析令牌
            map = Jwt.parseJWT(token);
            //移除iat和exp
            map.remove("iat");
            map.remove("exp");
            map.remove("class");
            //获取用户id
            String userId = map.get("id").toString();

            //判断令牌是否在黑名单中
            Boolean member = stringRedisTemplate.opsForSet().isMember(Constant.TOKEN_BLACKLIST_KEY, userId);
            if (member != null && member) {
                if (stringRedisTemplate.opsForValue().get(Constant.TOKEN_INVALID_KEY_PREFIX + userId) == null) {
                    //令牌不在黑名单中，移除缓存元素
                    stringRedisTemplate.opsForSet().remove(Constant.TOKEN_BLACKLIST_KEY, userId);
                }
                // 令牌在黑名单中，返回未认证响应
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 状态码
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"msg\":\"未认证，请先登录\"}");
                return false;
            }
            //获取令牌中class，判断是用户还是商户
            String aClass = map.get("class").toString();
            if (aClass.equals(MerchantDTO.class.getName())) {
                //商户
                //map转为MerchantDTO
                MerchantDTO merchantDTO = new MerchantDTO();
                BeanUtil.fillBeanWithMap(map, merchantDTO, false);
                //设置到thread Local中
                MerchantHolder.set(merchantDTO);
            } else {
                // 用户
                UserDTO userDTO = UserDTO.builder().build();
                BeanUtil.fillBeanWithMap(map, userDTO, false);
                UserHolder.set(userDTO);
            }

        } catch (JwtException e) {
            // 捕获 JWT 解析异常（过期、签名错误等）
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"令牌无效或已过期\"}");
            return false; // 中断流程
        }


        //放行
        return true;

    }

    //postHandle方法表示请求处理完毕，视图渲染开始，此时可以进行一些资源清理工作。

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    // afterCompletion方法表示请求处理完毕，视图渲染完毕，此时可以进行一些资源清理工作。
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //判断ex 是否为空
        if (ex != null) {
            log.error("请求处理异常：{}", ex.getMessage());
        }


        //清理thread Local
        MerchantHolder.remove();
        UserHolder.remove();

//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
