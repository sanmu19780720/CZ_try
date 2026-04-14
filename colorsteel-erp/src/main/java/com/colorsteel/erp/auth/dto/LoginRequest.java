package com.colorsteel.erp.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "登录请求")
public class LoginRequest {

    @NotBlank
    @Schema(description = "用户名", example = "admin")
    private String username;

    @NotBlank
    @Schema(description = "密码", example = "admin123")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
