package com.colorsteel.erp.portal.vo;

import com.colorsteel.erp.portal.entity.PortalProduct;

import java.math.BigDecimal;

public class PortalProductVO {
    private Long id;
    private Long productId;
    private Long categoryId;
    private String displayTitle;
    private String displayImage;
    private BigDecimal displayPrice;
    private String displayUnit;
    private String displayDesc;
    private Integer isFeatured;
    private Integer showOnHome;
    private Integer sortNo;
    private String status;

    public static PortalProductVO from(PortalProduct e) {
        PortalProductVO vo = new PortalProductVO();
        vo.id = e.getId();
        vo.productId = e.getProductId();
        vo.displayTitle = e.getDisplayTitle();
        vo.displayImage = e.getDisplayImage();
        vo.displayPrice = e.getDisplayPrice();
        vo.displayUnit = e.getDisplayUnit();
        vo.displayDesc = e.getDisplayDesc();
        vo.isFeatured = e.getIsFeatured();
        vo.showOnHome = e.getShowOnHome();
        vo.sortNo = e.getSortNo();
        vo.status = e.getStatus();
        return vo;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public Long getCategoryId() { return categoryId; }
    public String getDisplayTitle() { return displayTitle; }
    public String getDisplayImage() { return displayImage; }
    public BigDecimal getDisplayPrice() { return displayPrice; }
    public String getDisplayUnit() { return displayUnit; }
    public String getDisplayDesc() { return displayDesc; }
    public Integer getIsFeatured() { return isFeatured; }
    public Integer getShowOnHome() { return showOnHome; }
    public Integer getSortNo() { return sortNo; }
    public String getStatus() { return status; }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
