package com.colorsteel.erp.system.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "角色分页查询")
public class SysRoleQuery extends PageQuery {

    @Schema(description = "角色编码（模糊）")
    private String roleCode;

    @Schema(description = "角色名称（模糊）")
    private String roleName;

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
}
