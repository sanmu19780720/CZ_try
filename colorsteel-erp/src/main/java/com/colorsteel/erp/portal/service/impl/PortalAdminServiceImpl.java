package com.colorsteel.erp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.portal.dto.*;
import com.colorsteel.erp.portal.entity.*;
import com.colorsteel.erp.portal.mapper.*;
import com.colorsteel.erp.portal.service.PortalAdminService;
import com.colorsteel.erp.portal.vo.*;
import com.colorsteel.erp.product.entity.Product;
import com.colorsteel.erp.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class PortalAdminServiceImpl implements PortalAdminService {

    private static final Set<String> LINK_TYPES = Set.of("NONE", "PRODUCT", "CASE", "URL");
    private static final Set<String> DISPLAY_STATUS = Set.of("ENABLED", "DISABLED");
    private static final Set<String> INQUIRY_STATUS = Set.of("NEW", "FOLLOWING", "CLOSED");

    private final PortalSiteConfigMapper siteConfigMapper;
    private final PortalBannerMapper bannerMapper;
    private final PortalProductMapper productMapper;
    private final PortalCaseMapper caseMapper;
    private final PortalCaseImageMapper caseImageMapper;
    private final PortalPriceCardMapper priceCardMapper;
    private final PortalInquiryMapper inquiryMapper;
    private final ProductMapper erpProductMapper;

    public PortalAdminServiceImpl(
            PortalSiteConfigMapper siteConfigMapper,
            PortalBannerMapper bannerMapper,
            PortalProductMapper productMapper,
            PortalCaseMapper caseMapper,
            PortalCaseImageMapper caseImageMapper,
            PortalPriceCardMapper priceCardMapper,
            PortalInquiryMapper inquiryMapper,
            ProductMapper erpProductMapper) {
        this.siteConfigMapper = siteConfigMapper;
        this.bannerMapper = bannerMapper;
        this.productMapper = productMapper;
        this.caseMapper = caseMapper;
        this.caseImageMapper = caseImageMapper;
        this.priceCardMapper = priceCardMapper;
        this.inquiryMapper = inquiryMapper;
        this.erpProductMapper = erpProductMapper;
    }

    @Override
    public PortalSiteConfigVO getSiteConfig() {
        PortalSiteConfig e = siteConfigMapper.selectOne(new LambdaQueryWrapper<PortalSiteConfig>().orderByDesc(PortalSiteConfig::getId).last("limit 1"));
        return PortalSiteConfigVO.from(e);
    }

    @Override
    @Transactional
    public void updateSiteConfig(PortalSiteConfigUpdateRequest request) {
        PortalSiteConfig e = siteConfigMapper.selectOne(new LambdaQueryWrapper<PortalSiteConfig>().orderByDesc(PortalSiteConfig::getId).last("limit 1"));
        if (e == null) {
            e = new PortalSiteConfig();
            copySiteConfig(request, e);
            siteConfigMapper.insert(e);
            return;
        }
        copySiteConfig(request, e);
        siteConfigMapper.updateById(e);
    }

    @Override
    public PageResult<PortalBannerVO> pageBanners(PortalBannerQuery query) {
        Page<PortalBanner> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<PortalBanner> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getTitle()), PortalBanner::getTitle, query.getTitle());
        w.eq(StringUtils.hasText(query.getStatus()), PortalBanner::getStatus, query.getStatus());
        w.orderByAsc(PortalBanner::getSortNo).orderByDesc(PortalBanner::getId);
        Page<PortalBanner> result = bannerMapper.selectPage(page, w);
        return PageResult.of(result, result.getRecords().stream().map(PortalBannerVO::from).collect(Collectors.toList()));
    }

    @Override
    public Long createBanner(PortalBannerUpsertRequest request) {
        validateLinkType(request.getLinkType());
        validateDisplayStatus(request.getStatus());
        PortalBanner e = new PortalBanner();
        copyBanner(request, e);
        bannerMapper.insert(e);
        return e.getId();
    }

    @Override
    public void updateBanner(Long id, PortalBannerUpsertRequest request) {
        validateLinkType(request.getLinkType());
        validateDisplayStatus(request.getStatus());
        PortalBanner e = requireById(bannerMapper.selectById(id));
        copyBanner(request, e);
        bannerMapper.updateById(e);
    }

    @Override
    public void deleteBanner(Long id) {
        if (bannerMapper.deleteById(id) == 0) throw new BusinessException(ResultCode.NOT_FOUND);
    }

    @Override
    @Transactional
    public void sortBanners(PortalSortUpdateRequest request) {
        applySort(request, (id, sortNo) -> {
            PortalBanner e = requireById(bannerMapper.selectById(id));
            e.setSortNo(sortNo);
            bannerMapper.updateById(e);
        });
    }

    @Override
    public PageResult<PortalProductVO> pageProducts(PortalProductQuery query) {
        Page<PortalProduct> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<PortalProduct> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getDisplayTitle()), PortalProduct::getDisplayTitle, query.getDisplayTitle());
        w.eq(StringUtils.hasText(query.getStatus()), PortalProduct::getStatus, query.getStatus());
        w.eq(query.getShowOnHome() != null, PortalProduct::getShowOnHome, query.getShowOnHome());
        w.orderByAsc(PortalProduct::getSortNo).orderByDesc(PortalProduct::getId);
        Page<PortalProduct> result = productMapper.selectPage(page, w);
        return PageResult.of(result, result.getRecords().stream().map(PortalProductVO::from).collect(Collectors.toList()));
    }

    @Override
    public Long createProduct(PortalProductUpsertRequest request) {
        validateDisplayStatus(request.getStatus());
        validateErpProductExists(request.getProductId());
        PortalProduct e = new PortalProduct();
        copyProduct(request, e);
        productMapper.insert(e);
        return e.getId();
    }

    @Override
    public void updateProduct(Long id, PortalProductUpsertRequest request) {
        validateDisplayStatus(request.getStatus());
        validateErpProductExists(request.getProductId());
        PortalProduct e = requireById(productMapper.selectById(id));
        copyProduct(request, e);
        productMapper.updateById(e);
    }

    @Override
    public void deleteProduct(Long id) {
        if (productMapper.deleteById(id) == 0) throw new BusinessException(ResultCode.NOT_FOUND);
    }

    @Override
    @Transactional
    public void sortProducts(PortalSortUpdateRequest request) {
        applySort(request, (id, sortNo) -> {
            PortalProduct e = requireById(productMapper.selectById(id));
            e.setSortNo(sortNo);
            productMapper.updateById(e);
        });
    }

    @Override
    public void updateProductHome(Long id, Integer showOnHome) {
        PortalProduct e = requireById(productMapper.selectById(id));
        e.setShowOnHome(showOnHome == null ? 0 : showOnHome);
        productMapper.updateById(e);
    }

    @Override
    public PageResult<PortalCaseVO> pageCases(PortalCaseQuery query) {
        Page<PortalCase> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<PortalCase> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getCaseTitle()), PortalCase::getCaseTitle, query.getCaseTitle());
        w.eq(StringUtils.hasText(query.getCaseCategory()), PortalCase::getCaseCategory, query.getCaseCategory());
        w.eq(StringUtils.hasText(query.getStatus()), PortalCase::getStatus, query.getStatus());
        w.eq(query.getIsRecommended() != null, PortalCase::getIsRecommended, query.getIsRecommended());
        w.orderByAsc(PortalCase::getSortNo).orderByDesc(PortalCase::getId);
        Page<PortalCase> result = caseMapper.selectPage(page, w);
        List<Long> caseIds = result.getRecords().stream().map(PortalCase::getId).toList();
        Map<Long, List<PortalCaseImageVO>> imageMap = caseIds.isEmpty()
                ? Map.of()
                : caseImageMapper.selectList(new LambdaQueryWrapper<PortalCaseImage>()
                        .in(PortalCaseImage::getCaseId, caseIds)
                        .orderByAsc(PortalCaseImage::getSortNo).orderByAsc(PortalCaseImage::getId))
                .stream().map(PortalCaseImageVO::from).collect(Collectors.groupingBy(PortalCaseImageVO::getCaseId));
        List<PortalCaseVO> records = result.getRecords().stream().map(PortalCaseVO::from).peek(vo -> vo.setImages(imageMap.getOrDefault(vo.getId(), List.of()))).toList();
        return PageResult.of(result, records);
    }

    @Override
    public Long createCase(PortalCaseUpsertRequest request) {
        validateDisplayStatus(request.getStatus());
        PortalCase e = new PortalCase();
        copyCase(request, e);
        caseMapper.insert(e);
        return e.getId();
    }

    @Override
    public void updateCase(Long id, PortalCaseUpsertRequest request) {
        validateDisplayStatus(request.getStatus());
        PortalCase e = requireById(caseMapper.selectById(id));
        copyCase(request, e);
        caseMapper.updateById(e);
    }

    @Override
    @Transactional
    public void deleteCase(Long id) {
        if (caseMapper.deleteById(id) == 0) throw new BusinessException(ResultCode.NOT_FOUND);
        caseImageMapper.delete(new LambdaQueryWrapper<PortalCaseImage>().eq(PortalCaseImage::getCaseId, id));
    }

    @Override
    public Long addCaseImage(Long caseId, PortalCaseImageCreateRequest request) {
        requireById(caseMapper.selectById(caseId));
        PortalCaseImage e = new PortalCaseImage();
        e.setCaseId(caseId);
        e.setImageUrl(request.getImageUrl());
        e.setSortNo(request.getSortNo());
        caseImageMapper.insert(e);
        return e.getId();
    }

    @Override
    public void deleteCaseImage(Long caseId, Long imageId) {
        PortalCaseImage image = requireById(caseImageMapper.selectById(imageId));
        if (!caseId.equals(image.getCaseId())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "图片不属于该案例");
        caseImageMapper.deleteById(imageId);
    }

    @Override
    public void updateCaseRecommended(Long id, Integer isRecommended) {
        PortalCase e = requireById(caseMapper.selectById(id));
        e.setIsRecommended(isRecommended == null ? 0 : isRecommended);
        caseMapper.updateById(e);
    }

    @Override
    @Transactional
    public void sortCases(PortalSortUpdateRequest request) {
        applySort(request, (id, sortNo) -> {
            PortalCase e = requireById(caseMapper.selectById(id));
            e.setSortNo(sortNo);
            caseMapper.updateById(e);
        });
    }

    @Override
    public PageResult<PortalPriceCardVO> pagePriceCards(PortalPriceCardQuery query) {
        Page<PortalPriceCard> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<PortalPriceCard> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getTitle()), PortalPriceCard::getTitle, query.getTitle());
        w.eq(StringUtils.hasText(query.getStatus()), PortalPriceCard::getStatus, query.getStatus());
        w.orderByAsc(PortalPriceCard::getSortNo).orderByDesc(PortalPriceCard::getId);
        Page<PortalPriceCard> result = priceCardMapper.selectPage(page, w);
        return PageResult.of(result, result.getRecords().stream().map(PortalPriceCardVO::from).collect(Collectors.toList()));
    }

    @Override
    public Long createPriceCard(PortalPriceCardUpsertRequest request) {
        validateDisplayStatus(request.getStatus());
        PortalPriceCard e = new PortalPriceCard();
        copyPriceCard(request, e);
        priceCardMapper.insert(e);
        return e.getId();
    }

    @Override
    public void updatePriceCard(Long id, PortalPriceCardUpsertRequest request) {
        validateDisplayStatus(request.getStatus());
        PortalPriceCard e = requireById(priceCardMapper.selectById(id));
        copyPriceCard(request, e);
        priceCardMapper.updateById(e);
    }

    @Override
    public void deletePriceCard(Long id) {
        if (priceCardMapper.deleteById(id) == 0) throw new BusinessException(ResultCode.NOT_FOUND);
    }

    @Override
    @Transactional
    public void sortPriceCards(PortalSortUpdateRequest request) {
        applySort(request, (id, sortNo) -> {
            PortalPriceCard e = requireById(priceCardMapper.selectById(id));
            e.setSortNo(sortNo);
            priceCardMapper.updateById(e);
        });
    }

    @Override
    public PageResult<PortalInquiryVO> pageInquiries(PortalInquiryQuery query) {
        Page<PortalInquiry> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<PortalInquiry> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getCustomerName()), PortalInquiry::getCustomerName, query.getCustomerName());
        w.like(StringUtils.hasText(query.getMobile()), PortalInquiry::getMobile, query.getMobile());
        w.eq(StringUtils.hasText(query.getStatus()), PortalInquiry::getStatus, query.getStatus());
        w.orderByDesc(PortalInquiry::getId);
        Page<PortalInquiry> result = inquiryMapper.selectPage(page, w);
        return PageResult.of(result, result.getRecords().stream().map(PortalInquiryVO::from).collect(Collectors.toList()));
    }

    @Override
    public void followInquiry(Long id, PortalInquiryFollowRequest request) {
        validateInquiryStatus(request.getStatus());
        PortalInquiry e = requireById(inquiryMapper.selectById(id));
        e.setStatus(request.getStatus());
        e.setFollowUpNote(request.getFollowUpNote());
        inquiryMapper.updateById(e);
    }

    private void copySiteConfig(PortalSiteConfigUpdateRequest request, PortalSiteConfig e) {
        e.setSiteName(request.getSiteName());
        e.setSiteLogo(request.getSiteLogo());
        e.setContactPhone(request.getContactPhone());
        e.setContactWechat(request.getContactWechat());
        e.setContactAddress(request.getContactAddress());
        e.setSiteIntro(request.getSiteIntro());
        e.setShareTitle(request.getShareTitle());
        e.setShareDesc(request.getShareDesc());
        e.setShareImage(request.getShareImage());
    }

    private void copyBanner(PortalBannerUpsertRequest request, PortalBanner e) {
        e.setTitle(request.getTitle());
        e.setImageUrl(request.getImageUrl());
        e.setLinkType(request.getLinkType());
        e.setLinkValue(request.getLinkValue());
        e.setSortNo(request.getSortNo());
        e.setStatus(request.getStatus());
    }

    private void copyProduct(PortalProductUpsertRequest request, PortalProduct e) {
        e.setProductId(request.getProductId());
        e.setDisplayTitle(request.getDisplayTitle());
        e.setDisplayImage(request.getDisplayImage());
        e.setDisplayPrice(request.getDisplayPrice());
        e.setDisplayUnit(request.getDisplayUnit());
        e.setDisplayDesc(request.getDisplayDesc());
        e.setIsFeatured(request.getIsFeatured());
        e.setShowOnHome(request.getShowOnHome());
        e.setSortNo(request.getSortNo());
        e.setStatus(request.getStatus());
    }

    private void copyCase(PortalCaseUpsertRequest request, PortalCase e) {
        e.setCaseTitle(request.getCaseTitle());
        e.setCaseCategory(request.getCaseCategory());
        e.setCoverImage(request.getCoverImage());
        e.setCaseDesc(request.getCaseDesc());
        e.setProjectAddress(request.getProjectAddress());
        e.setIsRecommended(request.getIsRecommended());
        e.setShowOnHome(request.getShowOnHome());
        e.setSortNo(request.getSortNo());
        e.setStatus(request.getStatus());
    }

    private void copyPriceCard(PortalPriceCardUpsertRequest request, PortalPriceCard e) {
        e.setTitle(request.getTitle());
        e.setImageUrl(request.getImageUrl());
        e.setReferencePrice(request.getReferencePrice());
        e.setPriceUnit(request.getPriceUnit());
        e.setDescText(request.getDescText());
        e.setSortNo(request.getSortNo());
        e.setStatus(request.getStatus());
    }

    private void applySort(PortalSortUpdateRequest request, BiConsumer<Long, Integer> updater) {
        request.getItems().forEach(it -> updater.accept(it.getId(), it.getSortNo()));
    }

    private void validateLinkType(String linkType) {
        if (!LINK_TYPES.contains(linkType)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "linkType 无效");
        }
    }

    private void validateDisplayStatus(String status) {
        if (!DISPLAY_STATUS.contains(status)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "status 无效");
        }
    }

    private void validateInquiryStatus(String status) {
        if (!INQUIRY_STATUS.contains(status)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "留资状态无效");
        }
    }

    private void validateErpProductExists(Long productId) {
        if (productId == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "productId 必填");
        }
        Product p = erpProductMapper.selectById(productId);
        if (p == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "关联商品不存在");
        }
    }

    private <T> T requireById(T value) {
        if (value == null) throw new BusinessException(ResultCode.NOT_FOUND);
        return value;
    }
}
