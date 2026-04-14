package com.colorsteel.erp.portal.dto;

import jakarta.validation.constraints.NotBlank;

public class PortalInquiryCreateRequest {
    @NotBlank
    private String customerName;
    @NotBlank
    private String mobile;
    private String inquiryType;
    private String inquiryContent;
    private String sourcePage;
    private Long relatedProductId;
    private Long relatedCaseId;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(String inquiryType) {
        this.inquiryType = inquiryType;
    }

    public String getInquiryContent() {
        return inquiryContent;
    }

    public void setInquiryContent(String inquiryContent) {
        this.inquiryContent = inquiryContent;
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(String sourcePage) {
        this.sourcePage = sourcePage;
    }

    public Long getRelatedProductId() {
        return relatedProductId;
    }

    public void setRelatedProductId(Long relatedProductId) {
        this.relatedProductId = relatedProductId;
    }

    public Long getRelatedCaseId() {
        return relatedCaseId;
    }

    public void setRelatedCaseId(Long relatedCaseId) {
        this.relatedCaseId = relatedCaseId;
    }
}
