package com.colorsteel.erp.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;
    private long timestamp;

    public int getCode() {
        return code;
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return of(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> fail(int code, String message) {
        return of(code, message, null);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return of(resultCode.getCode(), resultCode.getMessage(), null);
    }

    private static <T> Result<T> of(int code, String message, T data) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        r.data = data;
        r.timestamp = System.currentTimeMillis();
        return r;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
