package com.itcast.myweb.service.impl.client;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.PhoneUtil;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.domain.dto.LoginDTO;
import com.itcast.myweb.domain.dto.UserDTO;
import com.itcast.myweb.domain.entity.User;
import com.itcast.myweb.common.exception.*;
import com.itcast.myweb.service.client.AuthService;
import com.itcast.myweb.service.common.IUserService;
import com.itcast.myweb.utils.CodeUtil;
import com.itcast.myweb.utils.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    // 用户
    private final IUserService userService;


    //redis
    private final StringRedisTemplate stringRedisTemplate;


    //验证码工具类
    private final CodeUtil codeUtil;


    //获取验证码
    @Override
    public String getCode(String contactPhone) {

        //判断手机号是否为空
        if (contactPhone == null || contactPhone.isEmpty()) {
            throw new NullPhoneException("手机号不能为空");
        }


        //判断手机号格式是否正确
        boolean mobile = PhoneUtil.isMobile(contactPhone);
        if (!mobile) {
            throw new PhoneFormatException("手机号格式有误");
        }

        //生成验证码
        return codeUtil.createCode(contactPhone);

    }


    //登陆
    @Override
    public String login(LoginDTO loginDTO) {


        String loginDTOCode = loginDTO.getCode();//验证码
        //判断验证码是否为空
        if (loginDTOCode == null || loginDTOCode.isEmpty()) {
            throw new NullCodeException("验证码不能为空");
        }

        String loginDTOContactPhone = loginDTO.getContactPhone();//手机号

        //判断手机号是否为空
        if (loginDTOContactPhone == null || loginDTOContactPhone.isEmpty()) {
            throw new NullPhoneException("手机号不能为空");
        }
        //判断手机号格式是否正确
        boolean mobile = PhoneUtil.isMobile(loginDTOContactPhone);
        if (!mobile) {
            throw new PhoneFormatException("手机号格式有误");
        }

        //校验验证码
        Integer checkCode = codeUtil.checkCode(loginDTOCode, loginDTOContactPhone);
        if (Objects.equals(checkCode, Constant.CODE_EMPTY)) {
            throw new NullCodeException(Constant.MSG_EMPTY);
        } else if (Objects.equals(checkCode, Constant.CODE_ERROR)) {
            throw new CodeIncorrectException(Constant.MSG_ERROR);
        } else if (Objects.equals(checkCode, Constant.CODE_EXPIRE)) {
            throw new CodeExpiredException(Constant.MSG_EXPIRE);
        }


        //根据手机号查询用户
        User userByPhone = userService.lambdaQuery()
                .eq(User::getPhone, loginDTO.getContactPhone())
                .eq(User::getStatus, Constant.ACCOUNT_NORMAL)
                .one();


        //用于创建用户token的map集合
        Map<String, Object> userMap = new HashMap<>();


        //判断用户是否存在
        if (userByPhone == null) {
            //用户不存在，完成注册
            //添加用户
            String tempPassword = UUID.randomUUID().toString().substring(0, 8);
            String nickName = Constant.NICK_NAME_PREFIX + UUID.randomUUID().toString().substring(0, 8);
            String image = Constant.DEFAULT_AVATAR_URL;
            String accountName = Constant.ACCOUNT_NAME_PREFIX + UUID.randomUUID().toString().substring(0, 10);
            User user = User.builder()
                    .phone(loginDTOContactPhone)//手机号
                    .accountName(accountName)//账户名
                    .password(tempPassword)//密码
                    .nick(nickName)//昵称
                    .image(image)//头像
                    .build();
            userService.save(user);
            userByPhone = user;
        }

        //用户存在，直接登录

        //填充map
        userMap.put("id", userByPhone.getId());
        if (userByPhone.getDefaultAddressId() != null) {
            userMap.put("addressId", userByPhone.getDefaultAddressId());
        }
        userMap.put("nickName", userByPhone.getNick());
        userMap.put("image", userByPhone.getImage());
        userMap.put("class", UserDTO.class.getName());

        //生成jwt令牌
        return Jwt.createJWT(userMap);

    }


    //登出
    @Override
    public void logout(String token) {

        //判断令牌是否为空
        if (token == null || token.isEmpty()) {
            throw new NullTokenException("当前状态未登录");
        }

        //解析令牌
        Map<String, Object> map = Jwt.parseJWT(token);
        //获取过期时间
        long expireTime = ((Number) map.get("exp")).longValue() * 1000;

        //得到剩余时间
        long remainTime = expireTime - System.currentTimeMillis();

        //获取用户id
        String userId = map.get("id").toString();

        //userId存入redis黑名单
        stringRedisTemplate.opsForSet().add(Constant.TOKEN_BLACKLIST_KEY, userId);

        //缓存失效令牌
        stringRedisTemplate.opsForValue().set(Constant.TOKEN_INVALID_KEY_PREFIX + userId, token, remainTime, java.util.concurrent.TimeUnit.MILLISECONDS);


    }


}
