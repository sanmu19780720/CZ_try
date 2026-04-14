package com.colorsteel.erp.inventory.vo;

import com.colorsteel.erp.inventory.entity.InventoryTxn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "库存流水")
public class InventoryTxnVO {

    private Long id;
    private String txnNo;
    private Long warehouseId;
    private Long productId;
    private String bizType;
    private String direction;
    private BigDecimal quantity;
    private BigDecimal unitCost;
    private BigDecimal amount;
    private BigDecimal beforeQuantity;
    private BigDecimal afterQuantity;
    private String refType;
    private Long refId;
    private String refBizNo;
    private String remark;
    private LocalDateTime createdAt;

    public static InventoryTxnVO from(InventoryTxn e) {
        if (e == null) {
            return null;
        }
        InventoryTxnVO vo = new InventoryTxnVO();
        vo.setId(e.getId());
        vo.setTxnNo(e.getTxnNo());
        vo.setWarehouseId(e.getWarehouseId());
        vo.setProductId(e.getProductId());
        vo.setBizType(e.getBizType());
        vo.setDirection(e.getDirection());
        vo.setQuantity(e.getQuantity());
        vo.setUnitCost(e.getUnitCost());
        vo.setAmount(e.getAmount());
        vo.setBeforeQuantity(e.getBeforeQuantity());
        vo.setAfterQuantity(e.getAfterQuantity());
        vo.setRefType(e.getRefType());
        vo.setRefId(e.getRefId());
        vo.setRefBizNo(e.getRefBizNo());
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

    public String getTxnNo() {
        return txnNo;
    }

    public void setTxnNo(String txnNo) {
        this.txnNo = txnNo;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBeforeQuantity() {
        return beforeQuantity;
    }

    public void setBeforeQuantity(BigDecimal beforeQuantity) {
        this.beforeQuantity = beforeQuantity;
    }

    public BigDecimal getAfterQuantity() {
        return afterQuantity;
    }

    public void setAfterQuantity(BigDecimal afterQuantity) {
        this.afterQuantity = afterQuantity;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public String getRefBizNo() {
        return refBizNo;
    }

    public void setRefBizNo(String refBizNo) {
        this.refBizNo = refBizNo;
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
