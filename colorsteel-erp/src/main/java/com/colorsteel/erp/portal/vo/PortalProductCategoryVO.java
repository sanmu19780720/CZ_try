package com.colorsteel.erp.portal.vo;

public class PortalProductCategoryVO {
    private Long id;
    private String name;

    public PortalProductCategoryVO() {
    }

    public PortalProductCategoryVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
