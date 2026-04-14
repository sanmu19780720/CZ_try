package com.colorsteel.erp.product.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.product.dto.ProductCreateRequest;
import com.colorsteel.erp.product.dto.ProductQuery;
import com.colorsteel.erp.product.dto.ProductUpdateRequest;
import com.colorsteel.erp.product.service.ProductService;
import com.colorsteel.erp.product.vo.ProductVO;
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

@Tag(name = "商品")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "分页查询商品")
    @GetMapping
    public Result<PageResult<ProductVO>> page(@Valid @ModelAttribute ProductQuery query) {
        return Result.ok(productService.pageProducts(query));
    }

    @Operation(summary = "商品详情")
    @GetMapping("/{id}")
    public Result<ProductVO> get(@PathVariable Long id) {
        return Result.ok(productService.getProduct(id));
    }

    @Operation(summary = "新增商品")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody ProductCreateRequest request) {
        return Result.ok(productService.createProduct(request));
    }

    @Operation(summary = "编辑商品")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        productService.updateProduct(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除商品（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.ok();
    }
}
