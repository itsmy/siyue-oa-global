package com.oa.common.vo;

import com.oa.common.enums.ResultCode;
import lombok.Data;

/**
 * 注意这里的result的所有private变量需要有get和set方法，不然也会返回json转换异常
 * java.lang.IllegalArgumentException: No converter found for return value of type，就是这个异常
 */
@Data
public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(ResultCode resultCode, Object data) {
        this(resultCode);
        this.data = data;
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
}
