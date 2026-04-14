package com.colorsteel.erp.sales.vo;

import com.colorsteel.erp.sales.entity.SalesOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "销售单摘要")
public class SalesOrderSummaryVO {

    private Long id;
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
    private LocalDateTime createdAt;

    public static SalesOrderSummaryVO from(SalesOrder e) {
        if (e == null) {
            return null;
        }
        SalesOrderSummaryVO vo = new SalesOrderSummaryVO();
        vo.setId(e.getId());
        vo.setSalesNo(e.getSalesNo());
        vo.setCustomerId(e.getCustomerId());
        vo.setWarehouseId(e.getWarehouseId());
        vo.setOrderDate(e.getOrderDate());
        vo.setStatus(e.getStatus());
        vo.setTotalAmount(e.getTotalAmount());
        vo.setTotalCostAmount(e.getTotalCostAmount());
        vo.setGrossProfit(e.getGrossProfit());
        vo.setReceivedAmount(e.getReceivedAmount());
        vo.setRemark(e.getRemark());
        vo.setCreatedAt(e.getCreatedAt());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
