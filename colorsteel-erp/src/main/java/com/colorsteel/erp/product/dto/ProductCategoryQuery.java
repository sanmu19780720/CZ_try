package com.colorsteel.erp.product.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "商品分类分页查询")
public class ProductCategoryQuery extends PageQuery {

    @Schema(description = "分类名称（模糊）")
    private String name;

    @Schema(description = "上级分类ID")
    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
