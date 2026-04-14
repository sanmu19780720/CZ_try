package com.colorsteel.erp.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.colorsteel.erp.common.domain.BaseEntity;

@TableName("sys_role")
public class SysRole extends BaseEntity {

    private String roleCode;
    private String roleName;
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
