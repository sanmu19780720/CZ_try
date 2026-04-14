package com.colorsteel.erp.portal.dto;

import com.colorsteel.erp.common.web.PageQuery;

public class PortalInquiryQuery extends PageQuery {
    private String customerName;
    private String mobile;
    private String status;

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
