package com.colorsteel.erp.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PortalCaseUpsertRequest {
    @NotBlank
    private String caseTitle;
    private String caseCategory;
    private String coverImage;
    private String caseDesc;
    private String projectAddress;
    @NotNull
    private Integer isRecommended;
    @NotNull
    private Integer showOnHome;
    @NotNull
    private Integer sortNo;
    @NotBlank
    private String status;

    public String getCaseTitle() { return caseTitle; }
    public void setCaseTitle(String caseTitle) { this.caseTitle = caseTitle; }
    public String getCaseCategory() { return caseCategory; }
    public void setCaseCategory(String caseCategory) { this.caseCategory = caseCategory; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public String getCaseDesc() { return caseDesc; }
    public void setCaseDesc(String caseDesc) { this.caseDesc = caseDesc; }
    public String getProjectAddress() { return projectAddress; }
    public void setProjectAddress(String projectAddress) { this.projectAddress = projectAddress; }
    public Integer getIsRecommended() { return isRecommended; }
    public void setIsRecommended(Integer isRecommended) { this.isRecommended = isRecommended; }
    public Integer getShowOnHome() { return showOnHome; }
    public void setShowOnHome(Integer showOnHome) { this.showOnHome = showOnHome; }
    public Integer getSortNo() { return sortNo; }
    public void setSortNo(Integer sortNo) { this.sortNo = sortNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
