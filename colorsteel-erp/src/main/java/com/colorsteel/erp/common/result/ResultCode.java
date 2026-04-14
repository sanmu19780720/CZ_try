package com.colorsteel.erp.common.result;

public enum ResultCode {
    SUCCESS(0, "OK"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或令牌无效"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "HTTP 方法不允许"),
    CONFLICT(409, "资源冲突"),
    INSUFFICIENT_STOCK(409, "库存不足"),
    INTERNAL_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
