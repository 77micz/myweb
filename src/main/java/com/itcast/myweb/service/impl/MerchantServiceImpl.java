package com.itcast.myweb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itcast.myweb.DTO.LoginDTO;
import com.itcast.myweb.DTO.MerchantDTO;
import com.itcast.myweb.DTO.RegisterDTO;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.entity.Merchant;
import com.itcast.myweb.mapper.MerchantMapper;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.MerchantService;
import com.itcast.myweb.utils.Code;
import com.itcast.myweb.utils.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;


@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {// 商户服务实现类

    @Autowired
    private MerchantMapper merchantMapper;// 商户Mapper


    @Autowired
    private StringRedisTemplate redisTemplate;// redis

    // 获取验证码
    @Override
    public Result getCode(String contactPhone) {
        log.info("获取验证码");

        //检查联系人手机号是否存在
        Merchant merchant = merchantMapper.getByPhone(contactPhone);
        if (merchant == null){
            return Result.error("联系人手机号不存在");
        }

        //生成验证码
        String code = Code.createCode();

        log.info("验证码:{}", code);

        //存入redis
        redisTemplate.opsForValue().set(Constant.CODE_KEY +contactPhone, code, Duration.ofMinutes(Constant.CODE_TIME));// 5分钟


        return Result.ok(code);
    }

    // 商户注册
    @Override
    public Result register(RegisterDTO registerDTO) {

        //日志
        log.info("商户注册{}", registerDTO);
        // 商户注册
        merchantMapper.register(registerDTO);
        return Result.ok();
    }

    // 商户登录
    @Override
    public Result login(LoginDTO loginDTO) {

        // 日志
        log.info("商户登录");

        //检查联系人手机号是否存在
        Merchant merchant = merchantMapper.getByPhone(loginDTO.getContactPhone());
        if (merchant == null){
            return Result.error("联系人手机号不存在");
        }

        // 检查验证码
        String code = redisTemplate.opsForValue().get(Constant.CODE_KEY +loginDTO.getContactPhone());
        if (!loginDTO.getCode().equals( code)){
            return Result.error("验证码错误");
        }

        //merchant中的id与merchantName转为map
        MerchantDTO merchantDTO = new MerchantDTO();
        BeanUtil.copyProperties(merchant, merchantDTO);
        Map<String, Object> dataMap = BeanUtil.beanToMap(merchantDTO);




        //生成JWT并返回
        return Result.ok(Jwt.createJWT(dataMap));

    }
}
