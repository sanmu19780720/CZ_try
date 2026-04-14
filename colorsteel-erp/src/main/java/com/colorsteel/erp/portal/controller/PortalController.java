package com.colorsteel.erp.portal.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.portal.dto.PortalInquiryCreateRequest;
import com.colorsteel.erp.portal.dto.PortalPublicCaseQuery;
import com.colorsteel.erp.portal.dto.PortalPublicProductQuery;
import com.colorsteel.erp.portal.service.PortalPublicService;
import com.colorsteel.erp.portal.vo.PortalCaseVO;
import com.colorsteel.erp.portal.vo.PortalHomeVO;
import com.colorsteel.erp.portal.vo.PortalProductCategoryVO;
import com.colorsteel.erp.portal.vo.PortalProductVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "门户前台")
@RestController
@RequestMapping("/api/portal")
public class PortalController {

    private final PortalPublicService portalPublicService;

    public PortalController(PortalPublicService portalPublicService) {
        this.portalPublicService = portalPublicService;
    }

    @GetMapping("/home")
    public Result<PortalHomeVO> home() {
        return Result.ok(portalPublicService.getHome());
    }

    @GetMapping("/product-categories")
    public Result<List<PortalProductCategoryVO>> productCategories() {
        return Result.ok(portalPublicService.listProductCategories());
    }

    @GetMapping("/products")
    public Result<PageResult<PortalProductVO>> products(@Valid @ModelAttribute PortalPublicProductQuery query) {
        return Result.ok(portalPublicService.pageProducts(query));
    }

    @GetMapping("/products/{id}")
    public Result<PortalProductVO> product(@PathVariable Long id) {
        return Result.ok(portalPublicService.getProduct(id));
    }

    @GetMapping("/cases")
    public Result<PageResult<PortalCaseVO>> cases(@Valid @ModelAttribute PortalPublicCaseQuery query) {
        return Result.ok(portalPublicService.pageCases(query));
    }

    @GetMapping("/cases/{id}")
    public Result<PortalCaseVO> caseDetail(@PathVariable Long id) {
        return Result.ok(portalPublicService.getCase(id));
    }

    @PostMapping("/inquiries")
    public Result<Long> createInquiry(@Valid @RequestBody PortalInquiryCreateRequest request) {
        return Result.ok(portalPublicService.createInquiry(request));
    }
}
