package com.itcast.myweb.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {//统一返回结果


    private Integer code;// 状态码,1成功,0失败
    private String msg;// 消息
    private Object data;// 数据



    //有返回数据的成功状态
    public static Result ok(Object data) {

        return new Result(1, "操作成功", data);

    }

    //无返回数据的成功状态
    public static Result ok() {
        return new Result(1, "操作成功", null);
    }


    //失败状态
    public static Result error(String errorMsg) {

        return new Result(0, errorMsg, null);

    }

    //失败状态
    public static Result error( Integer code,String errorMsg) {

        return new Result(code, errorMsg, null);

    }


}
