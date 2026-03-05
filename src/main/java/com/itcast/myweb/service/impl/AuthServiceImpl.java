package com.itcast.myweb.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.PhoneUtil;
import com.itcast.myweb.DTO.LoginDTO;
import com.itcast.myweb.DTO.UserDTO;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.entity.User;
import com.itcast.myweb.exception.*;
import com.itcast.myweb.mapper.UserMapper;
import com.itcast.myweb.service.AuthService;
import com.itcast.myweb.utils.CodeUtil;
import com.itcast.myweb.utils.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.itcast.myweb.common.Constant.IS_NOT_DELETED;


@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private CodeUtil codeUtil;


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
        if(Objects.equals(checkCode, Constant.CODE_EMPTY)) {
            throw new NullCodeException(Constant.MSG_EMPTY);
        }else if(Objects.equals(checkCode, Constant.CODE_ERROR)) {
            throw new CodeException(Constant.MSG_ERROR);
        }else if(Objects.equals(checkCode, Constant.CODE_EXPIRE)) {
            throw new CodeException(Constant.MSG_EXPIRE);
        }



        //根据手机号查询用户数量
        UserDTO userByPhone = userMapper.getUserByPhone(loginDTOContactPhone, IS_NOT_DELETED);
        Map<String, Object> userMap = new HashMap<>();

        //id
        Integer userId = -1;
        //昵称
        String nickName = null;
        //头像
        String image = null;

        //判断用户是否存在
        if (userByPhone == null) {
            //添加用户
            String tempPassword = UUID.randomUUID().toString().substring(0, 8);
            nickName = "nick_" + UUID.randomUUID().toString().substring(0, 8);
            image = Constant.DEFAULT_AVATAR_URL;
            userId = userMapper.createUser(User.builder()
                    .phone(loginDTOContactPhone)//手机号
                    .password(tempPassword)//密码
                    .nickName(nickName)//昵称
                    .image(image)//头像
                    .build());
        }

        //填充map
        userMap.put("id", userId);
        userMap.put("nickName", nickName);
        userMap.put("image", image);
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

        //解析令牌，获取过期时间
        Map<String, Object> map = Jwt.parseJWT(token);
        long expireTime = (long) map.get("exp");

        //得到剩余时间
        long remainTime = expireTime - System.currentTimeMillis();

        //存入redis黑名单
        stringRedisTemplate.opsForValue().set(Constant.TOKEN_BLACKLIST_KEY + token, "logout", remainTime, java.util.concurrent.TimeUnit.MILLISECONDS);


    }


}
