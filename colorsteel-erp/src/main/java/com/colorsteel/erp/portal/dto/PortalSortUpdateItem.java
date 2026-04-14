package com.colorsteel.erp.portal.dto;

import jakarta.validation.constraints.NotNull;

public class PortalSortUpdateItem {
    @NotNull
    private Long id;
    @NotNull
    private Integer sortNo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getSortNo() { return sortNo; }
    public void setSortNo(Integer sortNo) { this.sortNo = sortNo; }
}
