package com.colorsteel.erp.portal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "门户站点配置更新")
public class PortalSiteConfigUpdateRequest {

    @NotBlank
    private String siteName;
    private String siteLogo;
    private String contactPhone;
    private String contactWechat;
    private String contactAddress;
    private String siteIntro;
    private String shareTitle;
    private String shareDesc;
    private String shareImage;

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }
    public String getSiteLogo() { return siteLogo; }
    public void setSiteLogo(String siteLogo) { this.siteLogo = siteLogo; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactWechat() { return contactWechat; }
    public void setContactWechat(String contactWechat) { this.contactWechat = contactWechat; }
    public String getContactAddress() { return contactAddress; }
    public void setContactAddress(String contactAddress) { this.contactAddress = contactAddress; }
    public String getSiteIntro() { return siteIntro; }
    public void setSiteIntro(String siteIntro) { this.siteIntro = siteIntro; }
    public String getShareTitle() { return shareTitle; }
    public void setShareTitle(String shareTitle) { this.shareTitle = shareTitle; }
    public String getShareDesc() { return shareDesc; }
    public void setShareDesc(String shareDesc) { this.shareDesc = shareDesc; }
    public String getShareImage() { return shareImage; }
    public void setShareImage(String shareImage) { this.shareImage = shareImage; }
}
