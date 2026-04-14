package com.colorsteel.erp.system.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户分页查询")
public class SysUserQuery extends PageQuery {

    @Schema(description = "登录名（模糊）")
    private String username;

    @Schema(description = "姓名（模糊）")
    private String realName;

    @Schema(description = "手机（模糊）")
    private String phone;

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
}
