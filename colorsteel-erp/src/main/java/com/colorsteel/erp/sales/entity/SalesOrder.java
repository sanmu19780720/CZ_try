package com.colorsteel.erp.sales.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.colorsteel.erp.common.domain.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@TableName("sales_order")
public class SalesOrder extends BaseEntity {

    private String salesNo;
    private Long customerId;
    private Long warehouseId;
    private LocalDate orderDate;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal totalCostAmount;
    private BigDecimal grossProfit;
    private BigDecimal receivedAmount;
    private String remark;

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalCostAmount() {
        return totalCostAmount;
    }

    public void setTotalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
