package com.colorsteel.erp.portal.dto;

import com.colorsteel.erp.common.web.PageQuery;

public class PortalProductQuery extends PageQuery {
    private String displayTitle;
    private String status;
    private Integer showOnHome;

    public String getDisplayTitle() { return displayTitle; }
    public void setDisplayTitle(String displayTitle) { this.displayTitle = displayTitle; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getShowOnHome() { return showOnHome; }
    public void setShowOnHome(Integer showOnHome) { this.showOnHome = showOnHome; }
}
