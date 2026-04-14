package com.colorsteel.erp.supplier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "编辑供应商")
public class SupplierUpdateRequest {

    @NotBlank
    @Schema(description = "供应商名称")
    private String name;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
