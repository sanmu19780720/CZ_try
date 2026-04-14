package com.colorsteel.erp.sales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "创建销售单（草稿，不出库）")
public class SalesOrderCreateRequest {

    @Schema(description = "客户ID；与新建客户信息二选一")
    private Long customerId;

    @Schema(description = "客户名称（未传 customerId 时必填，将自动建档）")
    private String customerName;

    @Schema(description = "新客户手机")
    private String customerPhone;

    @Schema(description = "新客户联系人")
    private String contactName;

    @NotNull
    @Schema(description = "出库仓库ID")
    private Long warehouseId;

    @NotNull
    @Schema(description = "单据日期")
    private LocalDate orderDate;

    @NotEmpty
    @Valid
    @Schema(description = "明细行")
    private List<SalesOrderLineRequest> lines;

    @Schema(description = "备注")
    private String remark;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<SalesOrderLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<SalesOrderLineRequest> lines) {
        this.lines = lines;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
