package com.colorsteel.erp.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.colorsteel.erp.common.domain.BaseEntity;

@TableName("product_category")
public class ProductCategory extends BaseEntity {

    private Long parentId;
    private String name;
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
