package com.itcast.myweb.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.PhoneUtil;
import com.itcast.myweb.DTO.LoginDTO;
import com.itcast.myweb.DTO.UserDTO;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.entity.User;
import com.itcast.myweb.exception.*;
import com.itcast.myweb.mapper.UserMapper;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.AuthService;
import com.itcast.myweb.utils.Code;
import com.itcast.myweb.utils.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.itcast.myweb.common.Constant.IS_NOT_DELETED;


@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    //获取验证码
    @Override
    public String getCode(String contactPhone) {

        //判断手机号是否为空
        if (contactPhone == null || contactPhone.isEmpty()){
            throw new NullPhoneException("手机号不能为空");
        }

        String code = Code.createCode();

        //判断手机号格式是否正确
        boolean mobile = PhoneUtil.isMobile(contactPhone);
        if (!mobile){
            throw new PhoneFormatException("手机号格式有误");
        }

        //缓存验证码
        stringRedisTemplate.opsForValue().set(Constant.CODE_KEY + contactPhone, code, Constant.CODE_TIME, java.util.concurrent.TimeUnit.MINUTES);

        return code;
    }


    //登陆
    @Override
    public String login(LoginDTO loginDTO) {


        String loginDTOCode = loginDTO.getCode();//验证码
        //判断验证码是否为空
        if (loginDTOCode == null || loginDTOCode.isEmpty()){
            throw new NullCodeException("验证码不能为空");
        }

        String loginDTOContactPhone = loginDTO.getContactPhone();//手机号

        //判断手机号是否为空
        if (loginDTOContactPhone == null || loginDTOContactPhone.isEmpty()) {
            throw new NullPhoneException("手机号不能为空");
        }
        //判断手机号格式是否正确
        boolean mobile = PhoneUtil.isMobile(loginDTOContactPhone);
        if (!mobile){
            throw new PhoneFormatException("手机号格式有误");
        }

        //获取redis验证码
        String string = stringRedisTemplate.opsForValue().get(Constant.CODE_KEY + loginDTOContactPhone);
        //判断是否过期
        if (string == null) {
            throw new CodeExpiredException("验证码已过期");
        }
        //判断验证码是否相同
        if (!loginDTOCode.equals(string)) {
            throw new IncorrectCodeException("验证码错误");
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

        //生成jwt令牌
        return Jwt.createJWT(userMap);

    }


}
