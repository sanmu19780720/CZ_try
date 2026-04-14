package com.colorsteel.erp.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Schema(description = "新增商品")
public class ProductCreateRequest {

    @Schema(description = "分类ID")
    private Long categoryId;

    @NotBlank
    @Schema(description = "SKU")
    private String sku;

    @NotBlank
    @Schema(description = "品名")
    private String name;

    @Schema(description = "计量单位")
    private String unit;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "参考售价")
    private BigDecimal standardPrice;

    @Schema(description = "备注")
    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
