package com.colorsteel.erp.portal.dto;

import com.colorsteel.erp.common.web.PageQuery;

public class PortalCaseQuery extends PageQuery {
    private String caseTitle;
    private String caseCategory;
    private String status;
    private Integer isRecommended;

    public String getCaseTitle() { return caseTitle; }
    public void setCaseTitle(String caseTitle) { this.caseTitle = caseTitle; }
    public String getCaseCategory() { return caseCategory; }
    public void setCaseCategory(String caseCategory) { this.caseCategory = caseCategory; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getIsRecommended() { return isRecommended; }
    public void setIsRecommended(Integer isRecommended) { this.isRecommended = isRecommended; }
}
