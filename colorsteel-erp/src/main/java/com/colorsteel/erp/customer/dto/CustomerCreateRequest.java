package com.colorsteel.erp.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "新增客户")
public class CustomerCreateRequest {

    @NotBlank
    @Schema(description = "客户编号")
    private String customerNo;

    @NotBlank
    @Schema(description = "客户名称")
    private String name;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "备注")
    private String remark;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

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
