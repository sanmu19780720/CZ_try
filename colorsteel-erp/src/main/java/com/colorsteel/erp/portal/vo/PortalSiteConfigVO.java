package com.colorsteel.erp.portal.vo;

import com.colorsteel.erp.portal.entity.PortalSiteConfig;

public class PortalSiteConfigVO {
    private Long id;
    private String siteName;
    private String siteLogo;
    private String contactPhone;
    private String contactWechat;
    private String contactAddress;
    private String siteIntro;
    private String shareTitle;
    private String shareDesc;
    private String shareImage;

    public static PortalSiteConfigVO from(PortalSiteConfig e) {
        if (e == null) return null;
        PortalSiteConfigVO vo = new PortalSiteConfigVO();
        vo.id = e.getId();
        vo.siteName = e.getSiteName();
        vo.siteLogo = e.getSiteLogo();
        vo.contactPhone = e.getContactPhone();
        vo.contactWechat = e.getContactWechat();
        vo.contactAddress = e.getContactAddress();
        vo.siteIntro = e.getSiteIntro();
        vo.shareTitle = e.getShareTitle();
        vo.shareDesc = e.getShareDesc();
        vo.shareImage = e.getShareImage();
        return vo;
    }

    public Long getId() { return id; }
    public String getSiteName() { return siteName; }
    public String getSiteLogo() { return siteLogo; }
    public String getContactPhone() { return contactPhone; }
    public String getContactWechat() { return contactWechat; }
    public String getContactAddress() { return contactAddress; }
    public String getSiteIntro() { return siteIntro; }
    public String getShareTitle() { return shareTitle; }
    public String getShareDesc() { return shareDesc; }
    public String getShareImage() { return shareImage; }
}
