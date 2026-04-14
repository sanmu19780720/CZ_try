package com.colorsteel.erp.portal.vo;

import com.colorsteel.erp.portal.entity.PortalPriceCard;

import java.math.BigDecimal;

public class PortalPriceCardVO {
    private Long id;
    private String title;
    private String imageUrl;
    private BigDecimal referencePrice;
    private String priceUnit;
    private String descText;
    private Integer sortNo;
    private String status;

    public static PortalPriceCardVO from(PortalPriceCard e) {
        PortalPriceCardVO vo = new PortalPriceCardVO();
        vo.id = e.getId();
        vo.title = e.getTitle();
        vo.imageUrl = e.getImageUrl();
        vo.referencePrice = e.getReferencePrice();
        vo.priceUnit = e.getPriceUnit();
        vo.descText = e.getDescText();
        vo.sortNo = e.getSortNo();
        vo.status = e.getStatus();
        return vo;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public BigDecimal getReferencePrice() { return referencePrice; }
    public String getPriceUnit() { return priceUnit; }
    public String getDescText() { return descText; }
    public Integer getSortNo() { return sortNo; }
    public String getStatus() { return status; }
}
