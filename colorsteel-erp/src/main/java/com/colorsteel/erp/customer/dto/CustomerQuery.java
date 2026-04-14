package com.colorsteel.erp.customer.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "客户分页查询")
public class CustomerQuery extends PageQuery {

    @Schema(description = "客户编号（模糊）")
    private String customerNo;

    @Schema(description = "客户名称（模糊）")
    private String name;

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
}
