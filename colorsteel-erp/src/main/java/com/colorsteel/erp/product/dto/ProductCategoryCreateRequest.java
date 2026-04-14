package com.colorsteel.erp.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "新增商品分类")
public class ProductCategoryCreateRequest {

    @Schema(description = "上级分类ID")
    private Long parentId;

    @NotBlank
    @Schema(description = "分类名称")
    private String name;

    @NotNull
    @Schema(description = "排序")
    private Integer sortOrder;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
