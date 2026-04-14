package com.colorsteel.erp.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PortalProductUpsertRequest {
    @NotNull
    private Long productId;
    @NotBlank
    private String displayTitle;
    private String displayImage;
    private BigDecimal displayPrice;
    private String displayUnit;
    private String displayDesc;
    @NotNull
    private Integer isFeatured;
    @NotNull
    private Integer showOnHome;
    @NotNull
    private Integer sortNo;
    @NotBlank
    private String status;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getDisplayTitle() { return displayTitle; }
    public void setDisplayTitle(String displayTitle) { this.displayTitle = displayTitle; }
    public String getDisplayImage() { return displayImage; }
    public void setDisplayImage(String displayImage) { this.displayImage = displayImage; }
    public BigDecimal getDisplayPrice() { return displayPrice; }
    public void setDisplayPrice(BigDecimal displayPrice) { this.displayPrice = displayPrice; }
    public String getDisplayUnit() { return displayUnit; }
    public void setDisplayUnit(String displayUnit) { this.displayUnit = displayUnit; }
    public String getDisplayDesc() { return displayDesc; }
    public void setDisplayDesc(String displayDesc) { this.displayDesc = displayDesc; }
    public Integer getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Integer isFeatured) { this.isFeatured = isFeatured; }
    public Integer getShowOnHome() { return showOnHome; }
    public void setShowOnHome(Integer showOnHome) { this.showOnHome = showOnHome; }
    public Integer getSortNo() { return sortNo; }
    public void setSortNo(Integer sortNo) { this.sortNo = sortNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
