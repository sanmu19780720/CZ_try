package com.colorsteel.erp.system.vo;

import com.colorsteel.erp.system.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "角色展示")
public class SysRoleVO {

    private Long id;
    private String roleCode;
    private String roleName;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SysRoleVO from(SysRole e) {
        if (e == null) {
            return null;
        }
        SysRoleVO vo = new SysRoleVO();
        vo.setId(e.getId());
        vo.setRoleCode(e.getRoleCode());
        vo.setRoleName(e.getRoleName());
        vo.setRemark(e.getRemark());
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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
