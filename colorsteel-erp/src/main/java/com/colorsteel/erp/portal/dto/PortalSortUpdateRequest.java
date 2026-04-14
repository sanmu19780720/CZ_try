package com.colorsteel.erp.portal.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PortalSortUpdateRequest {
    @Valid
    @NotEmpty
    private List<PortalSortUpdateItem> items;

    public List<PortalSortUpdateItem> getItems() { return items; }
    public void setItems(List<PortalSortUpdateItem> items) { this.items = items; }
}
