package com.colorsteel.erp.portal.dto;

import com.colorsteel.erp.common.web.PageQuery;

public class PortalBannerQuery extends PageQuery {
    private String title;
    private String status;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
