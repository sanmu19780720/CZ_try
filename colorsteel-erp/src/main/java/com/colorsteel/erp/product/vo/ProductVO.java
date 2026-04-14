package com.colorsteel.erp.product.vo;

import com.colorsteel.erp.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "商品展示")
public class ProductVO {

    private Long id;
    private Long categoryId;
    private String sku;
    private String name;
    private String unit;
    private String spec;
    private BigDecimal standardPrice;
    @Schema(description = "最近采购单价")
    private BigDecimal lastPurchasePrice;
    @Schema(description = "全仓加权平均成本")
    private BigDecimal avgCostPrice;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductVO from(Product e) {
        if (e == null) {
            return null;
        }
        ProductVO vo = new ProductVO();
        vo.setId(e.getId());
        vo.setCategoryId(e.getCategoryId());
        vo.setSku(e.getSku());
        vo.setName(e.getName());
        vo.setUnit(e.getUnit());
        vo.setSpec(e.getSpec());
        vo.setStandardPrice(e.getStandardPrice());
        vo.setLastPurchasePrice(e.getLastPurchasePrice());
        vo.setAvgCostPrice(e.getAvgCostPrice());
        vo.setRemark(e.getRemark());
        vo.setCreatedAt(e.getCreatedAt());
        vo.setUpdatedAt(e.getUpdatedAt());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(BigDecimal standardPrice) {
        this.standardPrice = standardPrice;
    }

    public BigDecimal getLastPurchasePrice() {
        return lastPurchasePrice;
    }

    public void setLastPurchasePrice(BigDecimal lastPurchasePrice) {
        this.lastPurchasePrice = lastPurchasePrice;
    }

    public BigDecimal getAvgCostPrice() {
        return avgCostPrice;
    }

    public void setAvgCostPrice(BigDecimal avgCostPrice) {
        this.avgCostPrice = avgCostPrice;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
