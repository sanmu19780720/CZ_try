package com.colorsteel.erp.portal.vo;

import com.colorsteel.erp.portal.entity.PortalCaseImage;

public class PortalCaseImageVO {
    private Long id;
    private Long caseId;
    private String imageUrl;
    private Integer sortNo;

    public static PortalCaseImageVO from(PortalCaseImage e) {
        PortalCaseImageVO vo = new PortalCaseImageVO();
        vo.id = e.getId();
        vo.caseId = e.getCaseId();
        vo.imageUrl = e.getImageUrl();
        vo.sortNo = e.getSortNo();
        return vo;
    }

    public Long getId() { return id; }
    public Long getCaseId() { return caseId; }
    public String getImageUrl() { return imageUrl; }
    public Integer getSortNo() { return sortNo; }
}
