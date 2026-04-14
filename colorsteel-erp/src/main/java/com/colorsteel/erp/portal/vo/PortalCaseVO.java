package com.colorsteel.erp.portal.vo;

import com.colorsteel.erp.portal.entity.PortalCase;

import java.util.List;

public class PortalCaseVO {
    private Long id;
    private String caseTitle;
    private String caseCategory;
    private String coverImage;
    private String caseDesc;
    private String projectAddress;
    private Integer isRecommended;
    private Integer showOnHome;
    private Integer sortNo;
    private String status;
    private List<PortalCaseImageVO> images;

    public static PortalCaseVO from(PortalCase e) {
        PortalCaseVO vo = new PortalCaseVO();
        vo.id = e.getId();
        vo.caseTitle = e.getCaseTitle();
        vo.caseCategory = e.getCaseCategory();
        vo.coverImage = e.getCoverImage();
        vo.caseDesc = e.getCaseDesc();
        vo.projectAddress = e.getProjectAddress();
        vo.isRecommended = e.getIsRecommended();
        vo.showOnHome = e.getShowOnHome();
        vo.sortNo = e.getSortNo();
        vo.status = e.getStatus();
        return vo;
    }

    public Long getId() { return id; }
    public String getCaseTitle() { return caseTitle; }
    public String getCaseCategory() { return caseCategory; }
    public String getCoverImage() { return coverImage; }
    public String getCaseDesc() { return caseDesc; }
    public String getProjectAddress() { return projectAddress; }
    public Integer getIsRecommended() { return isRecommended; }
    public Integer getShowOnHome() { return showOnHome; }
    public Integer getSortNo() { return sortNo; }
    public String getStatus() { return status; }
    public List<PortalCaseImageVO> getImages() { return images; }
    public void setImages(List<PortalCaseImageVO> images) { this.images = images; }
}
