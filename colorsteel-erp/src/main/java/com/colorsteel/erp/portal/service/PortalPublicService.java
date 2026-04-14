package com.colorsteel.erp.portal.service;

import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.portal.dto.PortalInquiryCreateRequest;
import com.colorsteel.erp.portal.dto.PortalPublicCaseQuery;
import com.colorsteel.erp.portal.dto.PortalPublicProductQuery;
import com.colorsteel.erp.portal.vo.PortalCaseVO;
import com.colorsteel.erp.portal.vo.PortalHomeVO;
import com.colorsteel.erp.portal.vo.PortalProductCategoryVO;
import com.colorsteel.erp.portal.vo.PortalProductVO;

import java.util.List;

public interface PortalPublicService {
    PortalHomeVO getHome();
    List<PortalProductCategoryVO> listProductCategories();
    PageResult<PortalProductVO> pageProducts(PortalPublicProductQuery query);
    PortalProductVO getProduct(Long id);
    PageResult<PortalCaseVO> pageCases(PortalPublicCaseQuery query);
    PortalCaseVO getCase(Long id);
    Long createInquiry(PortalInquiryCreateRequest request);
}
