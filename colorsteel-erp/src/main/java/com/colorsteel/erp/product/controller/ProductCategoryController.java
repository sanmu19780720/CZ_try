package com.colorsteel.erp.product.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.product.dto.ProductCategoryCreateRequest;
import com.colorsteel.erp.product.dto.ProductCategoryQuery;
import com.colorsteel.erp.product.dto.ProductCategoryUpdateRequest;
import com.colorsteel.erp.product.service.ProductCategoryService;
import com.colorsteel.erp.product.vo.ProductCategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "商品分类")
@RestController
@RequestMapping("/api/v1/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @Operation(summary = "分页查询商品分类")
    @GetMapping
    public Result<PageResult<ProductCategoryVO>> page(@Valid @ModelAttribute ProductCategoryQuery query) {
        return Result.ok(productCategoryService.pageCategories(query));
    }

    @Operation(summary = "分类详情")
    @GetMapping("/{id}")
    public Result<ProductCategoryVO> get(@PathVariable Long id) {
        return Result.ok(productCategoryService.getCategory(id));
    }

    @Operation(summary = "新增分类")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody ProductCategoryCreateRequest request) {
        return Result.ok(productCategoryService.createCategory(request));
    }

    @Operation(summary = "编辑分类")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ProductCategoryUpdateRequest request) {
        productCategoryService.updateCategory(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除分类（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productCategoryService.deleteCategory(id);
        return Result.ok();
    }
}
