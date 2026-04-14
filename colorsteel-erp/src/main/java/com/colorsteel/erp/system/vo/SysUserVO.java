package com.colorsteel.erp.system.vo;

import com.colorsteel.erp.system.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "用户展示")
public class SysUserVO {

    private Long id;
    private String username;
    private String realName;
    private String phone;
    private Integer status;
    private Long roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SysUserVO from(SysUser e) {
        if (e == null) {
            return null;
        }
        SysUserVO vo = new SysUserVO();
        vo.setId(e.getId());
        vo.setUsername(e.getUsername());
        vo.setRealName(e.getRealName());
        vo.setPhone(e.getPhone());
        vo.setStatus(e.getStatus());
        vo.setRoleId(e.getRoleId());
        vo.setCreatedAt(e.getCreatedAt());
        vo.setUpdatedAt(e.getUpdatedAt());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
