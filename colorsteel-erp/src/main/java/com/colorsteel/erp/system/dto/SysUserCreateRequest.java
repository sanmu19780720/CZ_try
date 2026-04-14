package com.colorsteel.erp.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "新增用户")
public class SysUserCreateRequest {

    @NotBlank
    @Schema(description = "登录名")
    private String username;

    @NotBlank
    @Schema(description = "明文密码（服务端存储哈希）")
    private String password;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "手机")
    private String phone;

    @NotNull
    @Schema(description = "状态：1启用 0停用")
    private Integer status;

    @Schema(description = "角色ID")
    private Long roleId;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
