package com.itcast.myweb.handler;


import com.itcast.myweb.common.exception.*;
import com.itcast.myweb.common.pojo.Result;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {// 异常处理


    /**
     * 处理 JWT 相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(JwtException.class)
    public Result error(JwtException e) {
        if (e instanceof ExpiredJwtException) {
            return Result.error(401, "登录已过期，请重新登录");
        } else if (e instanceof SignatureException) {
            return Result.error(401, "令牌签名无效，拒绝访问");
        } else if (e instanceof MalformedJwtException) {
            return Result.error(401, "令牌格式错误");
        } else if (e instanceof UnsupportedJwtException) {
            return Result.error(401, "不支持的令牌类型");
        } else if (e instanceof PrematureJwtException) {
            return Result.error(401, "令牌暂未生效");
        } else if (e instanceof InvalidClaimException) {
            return Result.error(401, "令牌验证失败");
        } else {
            // 兜底捕获所有 JWT 异常
            return Result.error(401, "令牌解析失败：" + e.getMessage());
        }
    }


    /**
     * 处理验证码相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(CodeException.class)
    public Result error(CodeException e) {
        if (e instanceof CodeExpiredException) {
            return Result.error(0, "验证码已过期");
        } else if (e instanceof NullCodeException) {
            return Result.error(0, "验证码不能为空");
        } else if (e instanceof CodeIncorrectException) {
            return Result.error(0, "验证码错误");
        } else {
            return Result.error(0, "验证码异常：" + e.getMessage());
        }
    }


    /**
     * 处理手机号相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(PhoneException.class)
    public Result error(PhoneException e) {
        if (e instanceof PhoneFormatException) {
            return Result.error(0, "手机号格式有误");
        } else if (e instanceof NullPhoneException) {
            return Result.error(0, "手机号不能为空");
        } else {
            return Result.error(0, "手机号异常：" + e.getMessage());
        }
    }


    /**
     * 处理商品相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(ItemException.class)
    public Result error(ItemException e) {
        if (e instanceof ItemNotFoundException) {
            return Result.error(0, "商品不存在");
        } else if (e instanceof ItemNumExceedMaxException) {
            return Result.error(0, "商品数量超出最大限制");
        } else if (e instanceof ItemSkuNotFoundException) {
            return Result.error(0, "商品sku不存在");
        } else if (e instanceof ItemStockInsufficientException) {
            return Result.error(0, "商品库存不足");
        } else {
            return Result.error(0, "商品异常：" + e.getMessage());
        }
    }


    /**
     * 处理购物车相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(CartException.class)
    public Result error(CartException e) {
        if (e instanceof CartNumExceedMaxException) {
            return Result.error(0, "购物车商品数量超出最大限制");
        } else if (e instanceof CartNotFoundException) {
            return Result.error(0, "购物车商品不存在");
        } else if (e instanceof CartItemMissException) {
            return Result.error(0, "购物车商品缺失");
        } else {
            return Result.error(0, "购物车异常：" + e.getMessage());
        }
    }


    /**
     * 处理订单相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(OrderException.class)
    public Result error(OrderException e) {
        if (e instanceof OrderNotFoundException) {
            return Result.error(0, "订单不存在");
        } else if (e instanceof OrderStatusException) {
            return Result.error(0, "订单状态异常");
        } else if (e instanceof OrderItemNumberException) {
            return Result.error(0, "数量必须大于0");
        } else if (e instanceof OrderNumberInconsistentException) {
            return Result.error(0, "订单数不一致");
        } else if (e instanceof OrderDetailMissException) {
            return Result.error(0, "订单明细缺失");
        } else {
            return Result.error(0, "订单异常：" + e.getMessage());
        }
    }


    /**
     * 处理支付相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(PayException.class)
    public Result error(PayException e) {
        if (e instanceof PayOrderNotFoundException) {
            return Result.error(0, "支付订单不存在");
        } else if (e instanceof PayOrderStatusException) {
            return Result.error(0, "支付订单状态异常");
        } else if (e instanceof PayLockGetFailException) {
            return Result.error(0, "支付正在处理，请勿重复提交");
        } else if (e instanceof PayCancelLockGetFailException) {
            return Result.error(0, "取消支付正在处理，请勿重复提交");
        } else {
            return Result.error(0, "支付异常：" + e.getMessage());
        }
    }


    /**
     * 处理地址相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(AddressException.class)
    public Result error(AddressException e) {
        if (e instanceof AddressNotFoundException) {
            return Result.error(0, "地址不存在");
        } else if (e instanceof AddressMissException) {
            return Result.error(0, "地址缺失");
        } else if (e instanceof AddressNumberInconsistentException) {
            return Result.error(0, "地址数不一致");
        } else {
            return Result.error(0, "地址异常：" + e.getMessage());
        }
    }


    /**
     * 处理用户相关异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(UserException.class)
    public Result error(UserException e) {
        if (e instanceof UserNotFoundException) {
            return Result.error(0, "用户不存在");
        } else if (e instanceof UserPasswordErrorException) {
            return Result.error(0, "用户密码错误");
        } else if (e instanceof UserBalanceNotEnoughException) {
            return Result.error(0, "用户余额不足");
        } else {
            return Result.error(0, "用户异常：" + e.getMessage());
        }
    }


    /**
     * 处理其他异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        log.error("服务器错误：{}", e.getMessage());
        return Result.error("服务器错误");
    }


}
