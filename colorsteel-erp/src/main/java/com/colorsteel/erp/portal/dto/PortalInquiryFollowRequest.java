package com.colorsteel.erp.portal.dto;

import jakarta.validation.constraints.NotBlank;

public class PortalInquiryFollowRequest {
    @NotBlank
    private String status;
    private String followUpNote;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFollowUpNote() { return followUpNote; }
    public void setFollowUpNote(String followUpNote) { this.followUpNote = followUpNote; }
}
