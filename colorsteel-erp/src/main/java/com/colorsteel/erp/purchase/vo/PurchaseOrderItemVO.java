package com.colorsteel.erp.purchase.vo;

import com.colorsteel.erp.purchase.entity.PurchaseOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "采购明细")
public class PurchaseOrderItemVO {

    private Long id;
    private Integer lineNo;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private String remark;

    public static PurchaseOrderItemVO from(PurchaseOrderItem e) {
        if (e == null) {
            return null;
        }
        PurchaseOrderItemVO vo = new PurchaseOrderItemVO();
        vo.setId(e.getId());
        vo.setLineNo(e.getLineNo());
        vo.setProductId(e.getProductId());
        vo.setQuantity(e.getQuantity());
        vo.setUnitPrice(e.getUnitPrice());
        vo.setAmount(e.getAmount());
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
