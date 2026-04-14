package com.colorsteel.erp.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "新增角色")
public class SysRoleCreateRequest {

    @NotBlank
    @Schema(description = "角色编码")
    private String roleCode;

    @NotBlank
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "备注")
    private String remark;

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
}
