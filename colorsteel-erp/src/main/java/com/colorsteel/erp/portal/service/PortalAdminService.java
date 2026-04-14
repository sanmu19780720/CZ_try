package com.colorsteel.erp.portal.service;

import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.portal.dto.*;
import com.colorsteel.erp.portal.vo.*;

import java.util.List;

public interface PortalAdminService {
    PortalSiteConfigVO getSiteConfig();
    void updateSiteConfig(PortalSiteConfigUpdateRequest request);

    PageResult<PortalBannerVO> pageBanners(PortalBannerQuery query);
    Long createBanner(PortalBannerUpsertRequest request);
    void updateBanner(Long id, PortalBannerUpsertRequest request);
    void deleteBanner(Long id);
    void sortBanners(PortalSortUpdateRequest request);

    PageResult<PortalProductVO> pageProducts(PortalProductQuery query);
    Long createProduct(PortalProductUpsertRequest request);
    void updateProduct(Long id, PortalProductUpsertRequest request);
    void deleteProduct(Long id);
    void sortProducts(PortalSortUpdateRequest request);
    void updateProductHome(Long id, Integer showOnHome);

    PageResult<PortalCaseVO> pageCases(PortalCaseQuery query);
    Long createCase(PortalCaseUpsertRequest request);
    void updateCase(Long id, PortalCaseUpsertRequest request);
    void deleteCase(Long id);
    Long addCaseImage(Long caseId, PortalCaseImageCreateRequest request);
    void deleteCaseImage(Long caseId, Long imageId);
    void updateCaseRecommended(Long id, Integer isRecommended);
    void sortCases(PortalSortUpdateRequest request);

    PageResult<PortalPriceCardVO> pagePriceCards(PortalPriceCardQuery query);
    Long createPriceCard(PortalPriceCardUpsertRequest request);
    void updatePriceCard(Long id, PortalPriceCardUpsertRequest request);
    void deletePriceCard(Long id);
    void sortPriceCards(PortalSortUpdateRequest request);

    PageResult<PortalInquiryVO> pageInquiries(PortalInquiryQuery query);
    void followInquiry(Long id, PortalInquiryFollowRequest request);
}
