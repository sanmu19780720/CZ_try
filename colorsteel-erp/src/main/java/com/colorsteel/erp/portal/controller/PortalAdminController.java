package com.colorsteel.erp.portal.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.portal.dto.*;
import com.colorsteel.erp.portal.service.PortalAdminService;
import com.colorsteel.erp.portal.service.PortalFileService;
import com.colorsteel.erp.portal.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

@Tag(name = "门户管理")
@RestController
@RequestMapping("/api/admin/portal")
public class PortalAdminController {

    private final PortalAdminService portalAdminService;
    private final PortalFileService portalFileService;

    public PortalAdminController(PortalAdminService portalAdminService, PortalFileService portalFileService) {
        this.portalAdminService = portalAdminService;
        this.portalFileService = portalFileService;
    }

    @Operation(summary = "获取站点配置")
    @GetMapping("/site-config")
    public Result<PortalSiteConfigVO> getSiteConfig() {
        return Result.ok(portalAdminService.getSiteConfig());
    }

    @Operation(summary = "更新站点配置")
    @PutMapping("/site-config")
    public Result<Void> updateSiteConfig(@Valid @RequestBody PortalSiteConfigUpdateRequest request) {
        portalAdminService.updateSiteConfig(request);
        return Result.ok();
    }

    @Operation(summary = "上传门户图片")
    @PostMapping("/upload-image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return Result.ok(portalFileService.uploadImage(file));
    }

    @GetMapping("/banners")
    public Result<PageResult<PortalBannerVO>> pageBanners(@Valid @ModelAttribute PortalBannerQuery query) {
        return Result.ok(portalAdminService.pageBanners(query));
    }

    @PostMapping("/banners")
    public Result<Long> createBanner(@Valid @RequestBody PortalBannerUpsertRequest request) {
        return Result.ok(portalAdminService.createBanner(request));
    }

    @PutMapping("/banners/{id}")
    public Result<Void> updateBanner(@PathVariable Long id, @Valid @RequestBody PortalBannerUpsertRequest request) {
        portalAdminService.updateBanner(id, request);
        return Result.ok();
    }

    @DeleteMapping("/banners/{id}")
    public Result<Void> deleteBanner(@PathVariable Long id) {
        portalAdminService.deleteBanner(id);
        return Result.ok();
    }

    @PutMapping("/banners/sort")
    public Result<Void> sortBanners(@Valid @RequestBody PortalSortUpdateRequest request) {
        portalAdminService.sortBanners(request);
        return Result.ok();
    }

    @GetMapping("/products")
    public Result<PageResult<PortalProductVO>> pageProducts(@Valid @ModelAttribute PortalProductQuery query) {
        return Result.ok(portalAdminService.pageProducts(query));
    }

    @PostMapping("/products")
    public Result<Long> createProduct(@Valid @RequestBody PortalProductUpsertRequest request) {
        return Result.ok(portalAdminService.createProduct(request));
    }

    @PutMapping("/products/{id}")
    public Result<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody PortalProductUpsertRequest request) {
        portalAdminService.updateProduct(id, request);
        return Result.ok();
    }

    @DeleteMapping("/products/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        portalAdminService.deleteProduct(id);
        return Result.ok();
    }

    @PutMapping("/products/sort")
    public Result<Void> sortProducts(@Valid @RequestBody PortalSortUpdateRequest request) {
        portalAdminService.sortProducts(request);
        return Result.ok();
    }

    @PutMapping("/products/{id}/home")
    public Result<Void> updateProductHome(@PathVariable Long id, @RequestParam Integer showOnHome) {
        portalAdminService.updateProductHome(id, showOnHome);
        return Result.ok();
    }

    @GetMapping("/cases")
    public Result<PageResult<PortalCaseVO>> pageCases(@Valid @ModelAttribute PortalCaseQuery query) {
        return Result.ok(portalAdminService.pageCases(query));
    }

    @PostMapping("/cases")
    public Result<Long> createCase(@Valid @RequestBody PortalCaseUpsertRequest request) {
        return Result.ok(portalAdminService.createCase(request));
    }

    @PutMapping("/cases/{id}")
    public Result<Void> updateCase(@PathVariable Long id, @Valid @RequestBody PortalCaseUpsertRequest request) {
        portalAdminService.updateCase(id, request);
        return Result.ok();
    }

    @DeleteMapping("/cases/{id}")
    public Result<Void> deleteCase(@PathVariable Long id) {
        portalAdminService.deleteCase(id);
        return Result.ok();
    }

    @PostMapping("/cases/{id}/images")
    public Result<Long> addCaseImage(@PathVariable("id") Long caseId, @Valid @RequestBody PortalCaseImageCreateRequest request) {
        return Result.ok(portalAdminService.addCaseImage(caseId, request));
    }

    @DeleteMapping("/cases/{id}/images/{imageId}")
    public Result<Void> deleteCaseImage(@PathVariable("id") Long caseId, @PathVariable Long imageId) {
        portalAdminService.deleteCaseImage(caseId, imageId);
        return Result.ok();
    }

    @PutMapping("/cases/{id}/recommended")
    public Result<Void> updateCaseRecommended(@PathVariable Long id, @RequestParam Integer isRecommended) {
        portalAdminService.updateCaseRecommended(id, isRecommended);
        return Result.ok();
    }

    @PutMapping("/cases/sort")
    public Result<Void> sortCases(@Valid @RequestBody PortalSortUpdateRequest request) {
        portalAdminService.sortCases(request);
        return Result.ok();
    }

    @GetMapping("/price-cards")
    public Result<PageResult<PortalPriceCardVO>> pagePriceCards(@Valid @ModelAttribute PortalPriceCardQuery query) {
        return Result.ok(portalAdminService.pagePriceCards(query));
    }

    @PostMapping("/price-cards")
    public Result<Long> createPriceCard(@Valid @RequestBody PortalPriceCardUpsertRequest request) {
        return Result.ok(portalAdminService.createPriceCard(request));
    }

    @PutMapping("/price-cards/{id}")
    public Result<Void> updatePriceCard(@PathVariable Long id, @Valid @RequestBody PortalPriceCardUpsertRequest request) {
        portalAdminService.updatePriceCard(id, request);
        return Result.ok();
    }

    @DeleteMapping("/price-cards/{id}")
    public Result<Void> deletePriceCard(@PathVariable Long id) {
        portalAdminService.deletePriceCard(id);
        return Result.ok();
    }

    @PutMapping("/price-cards/sort")
    public Result<Void> sortPriceCards(@Valid @RequestBody PortalSortUpdateRequest request) {
        portalAdminService.sortPriceCards(request);
        return Result.ok();
    }

    @GetMapping("/inquiries")
    public Result<PageResult<PortalInquiryVO>> pageInquiries(@Valid @ModelAttribute PortalInquiryQuery query) {
        return Result.ok(portalAdminService.pageInquiries(query));
    }

    @PutMapping("/inquiries/{id}/follow")
    public Result<Void> followInquiry(@PathVariable Long id, @Valid @RequestBody PortalInquiryFollowRequest request) {
        portalAdminService.followInquiry(id, request);
        return Result.ok();
    }
}
