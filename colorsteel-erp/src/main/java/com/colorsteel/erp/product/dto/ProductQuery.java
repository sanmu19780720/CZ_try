package com.colorsteel.erp.product.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "商品分页查询")
public class ProductQuery extends PageQuery {

    @Schema(description = "SKU（模糊）")
    private String sku;

    @Schema(description = "品名（模糊）")
    private String name;

    @Schema(description = "分类ID")
    private Long categoryId;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
