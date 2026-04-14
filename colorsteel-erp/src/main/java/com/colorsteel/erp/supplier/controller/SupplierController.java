package com.colorsteel.erp.supplier.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.supplier.dto.SupplierCreateRequest;
import com.colorsteel.erp.supplier.dto.SupplierQuery;
import com.colorsteel.erp.supplier.dto.SupplierUpdateRequest;
import com.colorsteel.erp.supplier.service.SupplierService;
import com.colorsteel.erp.supplier.vo.SupplierVO;
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

@Tag(name = "供应商")
@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(summary = "分页查询供应商")
    @GetMapping
    public Result<PageResult<SupplierVO>> page(@Valid @ModelAttribute SupplierQuery query) {
        return Result.ok(supplierService.pageSuppliers(query));
    }

    @Operation(summary = "供应商详情")
    @GetMapping("/{id}")
    public Result<SupplierVO> get(@PathVariable Long id) {
        return Result.ok(supplierService.getSupplier(id));
    }

    @Operation(summary = "新增供应商")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody SupplierCreateRequest request) {
        return Result.ok(supplierService.createSupplier(request));
    }

    @Operation(summary = "编辑供应商")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SupplierUpdateRequest request) {
        supplierService.updateSupplier(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除供应商（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return Result.ok();
    }
}
