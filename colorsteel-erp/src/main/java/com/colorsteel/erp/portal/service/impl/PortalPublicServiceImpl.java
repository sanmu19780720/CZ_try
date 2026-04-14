package com.colorsteel.erp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.portal.dto.PortalInquiryCreateRequest;
import com.colorsteel.erp.portal.dto.PortalPublicCaseQuery;
import com.colorsteel.erp.portal.dto.PortalPublicProductQuery;
import com.colorsteel.erp.portal.entity.*;
import com.colorsteel.erp.portal.mapper.*;
import com.colorsteel.erp.portal.service.PortalPublicService;
import com.colorsteel.erp.portal.vo.*;
import com.colorsteel.erp.product.entity.Product;
import com.colorsteel.erp.product.entity.ProductCategory;
import com.colorsteel.erp.product.mapper.ProductCategoryMapper;
import com.colorsteel.erp.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PortalPublicServiceImpl implements PortalPublicService {

    private final PortalSiteConfigMapper siteConfigMapper;
    private final PortalBannerMapper bannerMapper;
    private final PortalProductMapper portalProductMapper;
    private final PortalCaseMapper portalCaseMapper;
    private final PortalCaseImageMapper portalCaseImageMapper;
    private final PortalPriceCardMapper portalPriceCardMapper;
    private final PortalInquiryMapper portalInquiryMapper;
    private final ProductMapper productMapper;
    private final ProductCategoryMapper productCategoryMapper;

    public PortalPublicServiceImpl(
            PortalSiteConfigMapper siteConfigMapper,
            PortalBannerMapper bannerMapper,
            PortalProductMapper portalProductMapper,
            PortalCaseMapper portalCaseMapper,
            PortalCaseImageMapper portalCaseImageMapper,
            PortalPriceCardMapper portalPriceCardMapper,
            PortalInquiryMapper portalInquiryMapper,
            ProductMapper productMapper,
            ProductCategoryMapper productCategoryMapper) {
        this.siteConfigMapper = siteConfigMapper;
        this.bannerMapper = bannerMapper;
        this.portalProductMapper = portalProductMapper;
        this.portalCaseMapper = portalCaseMapper;
        this.portalCaseImageMapper = portalCaseImageMapper;
        this.portalPriceCardMapper = portalPriceCardMapper;
        this.portalInquiryMapper = portalInquiryMapper;
        this.productMapper = productMapper;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public PortalHomeVO getHome() {
        PortalSiteConfig site = siteConfigMapper.selectOne(
                new LambdaQueryWrapper<PortalSiteConfig>()
                        .orderByDesc(PortalSiteConfig::getId)
                        .last("limit 1")
        );
        List<PortalBannerVO> banners = bannerMapper.selectList(
                new LambdaQueryWrapper<PortalBanner>()
                        .eq(PortalBanner::getStatus, "ENABLED")
                        .orderByAsc(PortalBanner::getSortNo)
                        .orderByDesc(PortalBanner::getId)
        ).stream().map(PortalBannerVO::from).toList();
        List<PortalProductVO> featuredProducts = portalProductMapper.selectList(
                new LambdaQueryWrapper<PortalProduct>()
                        .eq(PortalProduct::getStatus, "ENABLED")
                        .eq(PortalProduct::getIsFeatured, 1)
                        .orderByAsc(PortalProduct::getSortNo)
                        .orderByDesc(PortalProduct::getId)
        ).stream().map(PortalProductVO::from).toList();
        fillProductCategory(featuredProducts);
        List<PortalPriceCardVO> priceCards = portalPriceCardMapper.selectList(
                new LambdaQueryWrapper<PortalPriceCard>()
                        .eq(PortalPriceCard::getStatus, "ENABLED")
                        .orderByAsc(PortalPriceCard::getSortNo)
                        .orderByDesc(PortalPriceCard::getId)
        ).stream().map(PortalPriceCardVO::from).toList();
        List<PortalCaseVO> recommendedCases = portalCaseMapper.selectList(
                new LambdaQueryWrapper<PortalCase>()
                        .eq(PortalCase::getStatus, "ENABLED")
                        .eq(PortalCase::getIsRecommended, 1)
                        .orderByAsc(PortalCase::getSortNo)
                        .orderByDesc(PortalCase::getId)
        ).stream().map(PortalCaseVO::from).toList();

        PortalHomeVO vo = new PortalHomeVO();
        vo.setSiteConfig(PortalSiteConfigVO.from(site));
        vo.setBanners(banners);
        vo.setFeaturedProducts(featuredProducts);
        vo.setPriceCards(priceCards);
        vo.setRecommendedCases(recommendedCases);

        PortalContactInfoVO contactInfo = new PortalContactInfoVO();
        if (site != null) {
            contactInfo.setContactPhone(site.getContactPhone());
            contactInfo.setContactWechat(site.getContactWechat());
            contactInfo.setContactAddress(site.getContactAddress());
        }
        vo.setContactInfo(contactInfo);
        return vo;
    }

    @Override
    public List<PortalProductCategoryVO> listProductCategories() {
        List<Long> productIds = portalProductMapper.selectList(
                new LambdaQueryWrapper<PortalProduct>().eq(PortalProduct::getStatus, "ENABLED")
        ).stream().map(PortalProduct::getProductId).filter(it -> it != null).distinct().toList();
        if (productIds.isEmpty()) {
            return List.of();
        }
        List<Long> categoryIds = productMapper.selectList(
                new LambdaQueryWrapper<Product>().in(Product::getId, productIds)
        ).stream().map(Product::getCategoryId).filter(it -> it != null).distinct().toList();
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        return productCategoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>().in(ProductCategory::getId, categoryIds).orderByAsc(ProductCategory::getSortOrder).orderByAsc(ProductCategory::getId)
        ).stream().map(it -> new PortalProductCategoryVO(it.getId(), it.getName())).toList();
    }

    @Override
    public PageResult<PortalProductVO> pageProducts(PortalPublicProductQuery query) {
        Page<PortalProduct> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<PortalProduct> w = new LambdaQueryWrapper<>();
        w.eq(PortalProduct::getStatus, "ENABLED");

        if (query.getCategoryId() != null) {
            List<Long> productIds = productMapper.selectList(
                    new LambdaQueryWrapper<Product>().eq(Product::getCategoryId, query.getCategoryId())
            ).stream().map(Product::getId).toList();
            if (productIds.isEmpty()) {
                return PageResult.of(page, List.of());
            }
            w.in(PortalProduct::getProductId, productIds);
        }

        w.orderByAsc(PortalProduct::getSortNo).orderByDesc(PortalProduct::getId);
        Page<PortalProduct> result = portalProductMapper.selectPage(page, w);
        List<PortalProductVO> records = result.getRecords().stream().map(PortalProductVO::from).toList();
        fillProductCategory(records);
        return PageResult.of(result, records);
    }

    @Override
    public PortalProductVO getProduct(Long id) {
        PortalProduct product = portalProductMapper.selectById(id);
        if (product == null || !"ENABLED".equals(product.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        PortalProductVO vo = PortalProductVO.from(product);
        fillProductCategory(List.of(vo));
        return vo;
    }

    @Override
    public PageResult<PortalCaseVO> pageCases(PortalPublicCaseQuery query) {
        Page<PortalCase> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<PortalCase> w = new LambdaQueryWrapper<>();
        w.eq(PortalCase::getStatus, "ENABLED");
        w.eq(StringUtils.hasText(query.getCaseCategory()), PortalCase::getCaseCategory, query.getCaseCategory());
        w.orderByAsc(PortalCase::getSortNo).orderByDesc(PortalCase::getId);
        Page<PortalCase> result = portalCaseMapper.selectPage(page, w);

        List<Long> caseIds = result.getRecords().stream().map(PortalCase::getId).toList();
        Map<Long, List<PortalCaseImageVO>> imageMap = caseIds.isEmpty() ? Map.of() :
                portalCaseImageMapper.selectList(
                                new LambdaQueryWrapper<PortalCaseImage>()
                                        .in(PortalCaseImage::getCaseId, caseIds)
                                        .orderByAsc(PortalCaseImage::getSortNo)
                                        .orderByAsc(PortalCaseImage::getId))
                        .stream().map(PortalCaseImageVO::from).collect(Collectors.groupingBy(PortalCaseImageVO::getCaseId));

        List<PortalCaseVO> records = result.getRecords().stream()
                .map(PortalCaseVO::from)
                .peek(it -> it.setImages(imageMap.getOrDefault(it.getId(), List.of())))
                .toList();
        return PageResult.of(result, records);
    }

    @Override
    public PortalCaseVO getCase(Long id) {
        PortalCase e = portalCaseMapper.selectById(id);
        if (e == null || !"ENABLED".equals(e.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        PortalCaseVO vo = PortalCaseVO.from(e);
        List<PortalCaseImageVO> images = portalCaseImageMapper.selectList(
                new LambdaQueryWrapper<PortalCaseImage>()
                        .eq(PortalCaseImage::getCaseId, id)
                        .orderByAsc(PortalCaseImage::getSortNo)
                        .orderByAsc(PortalCaseImage::getId)
        ).stream().map(PortalCaseImageVO::from).toList();
        vo.setImages(images);
        return vo;
    }

    @Override
    @Transactional
    public Long createInquiry(PortalInquiryCreateRequest request) {
        PortalInquiry e = new PortalInquiry();
        e.setCustomerName(request.getCustomerName());
        e.setMobile(request.getMobile());
        e.setInquiryType(request.getInquiryType());
        e.setInquiryContent(request.getInquiryContent());
        e.setSourcePage(request.getSourcePage());
        e.setRelatedProductId(request.getRelatedProductId());
        e.setRelatedCaseId(request.getRelatedCaseId());
        e.setStatus("NEW");
        portalInquiryMapper.insert(e);
        return e.getId();
    }

    private void fillProductCategory(List<PortalProductVO> vos) {
        List<Long> productIds = vos.stream().map(PortalProductVO::getProductId).filter(it -> it != null).distinct().toList();
        if (productIds.isEmpty()) {
            return;
        }
        Map<Long, Long> categoryMap = productMapper.selectList(
                new LambdaQueryWrapper<Product>().in(Product::getId, productIds)
        ).stream().collect(Collectors.toMap(Product::getId, Product::getCategoryId, (a, b) -> a));
        vos.forEach(it -> it.setCategoryId(categoryMap.get(it.getProductId())));
    }
}
