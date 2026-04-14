package com.colorsteel.erp.portal.vo;

import java.util.List;

public class PortalHomeVO {
    private PortalSiteConfigVO siteConfig;
    private List<PortalBannerVO> banners;
    private List<PortalProductVO> featuredProducts;
    private List<PortalPriceCardVO> priceCards;
    private List<PortalCaseVO> recommendedCases;
    private PortalContactInfoVO contactInfo;

    public PortalSiteConfigVO getSiteConfig() {
        return siteConfig;
    }

    public void setSiteConfig(PortalSiteConfigVO siteConfig) {
        this.siteConfig = siteConfig;
    }

    public List<PortalBannerVO> getBanners() {
        return banners;
    }

    public void setBanners(List<PortalBannerVO> banners) {
        this.banners = banners;
    }

    public List<PortalProductVO> getFeaturedProducts() {
        return featuredProducts;
    }

    public void setFeaturedProducts(List<PortalProductVO> featuredProducts) {
        this.featuredProducts = featuredProducts;
    }

    public List<PortalPriceCardVO> getPriceCards() {
        return priceCards;
    }

    public void setPriceCards(List<PortalPriceCardVO> priceCards) {
        this.priceCards = priceCards;
    }

    public List<PortalCaseVO> getRecommendedCases() {
        return recommendedCases;
    }

    public void setRecommendedCases(List<PortalCaseVO> recommendedCases) {
        this.recommendedCases = recommendedCases;
    }

    public PortalContactInfoVO getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(PortalContactInfoVO contactInfo) {
        this.contactInfo = contactInfo;
    }
}
