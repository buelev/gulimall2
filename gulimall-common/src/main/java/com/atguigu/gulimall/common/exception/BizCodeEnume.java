package com.atguigu.gulimall.common.exception;

/**
 * description: BizCodeEnume
 * date: 2020-08-17 08:14
 * author: buelev
 * version: 1.0
 * 异常类
 * 10:通用异常
 * 11:商品异常
 * 12:订单异常
 * 13:购物车
 * 14:物流
 */
public enum BizCodeEnume {
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数校验格式异常");
    private Integer code;
    private String msg;

    BizCodeEnume(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
