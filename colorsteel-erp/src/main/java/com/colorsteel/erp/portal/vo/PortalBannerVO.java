package com.colorsteel.erp.portal.vo;

import com.colorsteel.erp.portal.entity.PortalBanner;

public class PortalBannerVO {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkType;
    private String linkValue;
    private Integer sortNo;
    private String status;

    public static PortalBannerVO from(PortalBanner e) {
        PortalBannerVO vo = new PortalBannerVO();
        vo.id = e.getId();
        vo.title = e.getTitle();
        vo.imageUrl = e.getImageUrl();
        vo.linkType = e.getLinkType();
        vo.linkValue = e.getLinkValue();
        vo.sortNo = e.getSortNo();
        vo.status = e.getStatus();
        return vo;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getLinkType() { return linkType; }
    public String getLinkValue() { return linkValue; }
    public Integer getSortNo() { return sortNo; }
    public String getStatus() { return status; }
}
