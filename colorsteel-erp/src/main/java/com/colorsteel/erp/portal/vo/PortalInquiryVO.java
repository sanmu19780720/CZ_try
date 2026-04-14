package com.colorsteel.erp.portal.vo;

import com.colorsteel.erp.portal.entity.PortalInquiry;

import java.time.LocalDateTime;

public class PortalInquiryVO {
    private Long id;
    private String customerName;
    private String mobile;
    private String inquiryType;
    private String inquiryContent;
    private String sourcePage;
    private Long relatedProductId;
    private Long relatedCaseId;
    private String status;
    private String followUpNote;
    private LocalDateTime createdAt;

    public static PortalInquiryVO from(PortalInquiry e) {
        PortalInquiryVO vo = new PortalInquiryVO();
        vo.id = e.getId();
        vo.customerName = e.getCustomerName();
        vo.mobile = e.getMobile();
        vo.inquiryType = e.getInquiryType();
        vo.inquiryContent = e.getInquiryContent();
        vo.sourcePage = e.getSourcePage();
        vo.relatedProductId = e.getRelatedProductId();
        vo.relatedCaseId = e.getRelatedCaseId();
        vo.status = e.getStatus();
        vo.followUpNote = e.getFollowUpNote();
        vo.createdAt = e.getCreatedAt();
        return vo;
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getMobile() { return mobile; }
    public String getInquiryType() { return inquiryType; }
    public String getInquiryContent() { return inquiryContent; }
    public String getSourcePage() { return sourcePage; }
    public Long getRelatedProductId() { return relatedProductId; }
    public Long getRelatedCaseId() { return relatedCaseId; }
    public String getStatus() { return status; }
    public String getFollowUpNote() { return followUpNote; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
