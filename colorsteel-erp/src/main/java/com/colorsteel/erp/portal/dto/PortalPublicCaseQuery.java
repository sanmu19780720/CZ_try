package com.colorsteel.erp.portal.dto;

import com.colorsteel.erp.common.web.PageQuery;

public class PortalPublicCaseQuery extends PageQuery {
    private String caseCategory;

    public String getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(String caseCategory) {
        this.caseCategory = caseCategory;
    }
}
