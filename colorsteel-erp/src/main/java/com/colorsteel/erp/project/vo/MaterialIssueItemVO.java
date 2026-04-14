package com.colorsteel.erp.project.vo;

import com.colorsteel.erp.project.entity.ProjectMaterialIssueItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "项目领料明细")
public class MaterialIssueItemVO {

    private Long id;
    private Integer lineNo;
    private Long productId;
    private BigDecimal quantity;
    @Schema(description = "审核出库时固化的单位成本（成本价）")
    private BigDecimal costPrice;
    @Schema(description = "行材料成本")
    private BigDecimal costAmount;
    private BigDecimal amount;
    private String remark;

    public static MaterialIssueItemVO from(ProjectMaterialIssueItem e) {
        if (e == null) {
            return null;
        }
        MaterialIssueItemVO vo = new MaterialIssueItemVO();
        vo.setId(e.getId());
        vo.setLineNo(e.getLineNo());
        vo.setProductId(e.getProductId());
        vo.setQuantity(e.getQuantity());
        vo.setCostPrice(e.getUnitCost());
        vo.setCostAmount(e.getCostAmount() != null ? e.getCostAmount() : e.getAmount());
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

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
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
