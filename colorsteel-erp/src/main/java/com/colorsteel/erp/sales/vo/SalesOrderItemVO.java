package com.colorsteel.erp.sales.vo;

import com.colorsteel.erp.sales.entity.SalesOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "销售明细")
public class SalesOrderItemVO {

    private Long id;
    private Integer lineNo;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    @Schema(description = "审核出库时固化的单位成本（对应库字段 unit_cost）")
    private BigDecimal costPrice;
    private BigDecimal amount;
    private BigDecimal costAmount;
    private BigDecimal grossProfit;
    private String remark;

    public static SalesOrderItemVO from(SalesOrderItem e) {
        if (e == null) {
            return null;
        }
        SalesOrderItemVO vo = new SalesOrderItemVO();
        vo.setId(e.getId());
        vo.setLineNo(e.getLineNo());
        vo.setProductId(e.getProductId());
        vo.setQuantity(e.getQuantity());
        vo.setUnitPrice(e.getUnitPrice());
        vo.setCostPrice(e.getUnitCost());
        vo.setAmount(e.getAmount());
        vo.setCostAmount(e.getCostAmount());
        vo.setGrossProfit(e.getGrossProfit());
        vo.setRemark(e.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
