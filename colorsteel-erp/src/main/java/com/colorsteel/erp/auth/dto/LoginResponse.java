package com.colorsteel.erp.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "JWT")
    private String token;

    @Schema(description = "过期毫秒数")
    private long expiresInMs;

    public LoginResponse() {
    }

    public LoginResponse(String token, long expiresInMs) {
        this.token = token;
        this.expiresInMs = expiresInMs;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresInMs() {
        return expiresInMs;
    }

    public void setExpiresInMs(long expiresInMs) {
        this.expiresInMs = expiresInMs;
    }
}
