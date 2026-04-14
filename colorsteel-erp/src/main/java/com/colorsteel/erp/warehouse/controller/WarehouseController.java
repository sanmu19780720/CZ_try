package com.colorsteel.erp.warehouse.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.warehouse.dto.WarehouseCreateRequest;
import com.colorsteel.erp.warehouse.dto.WarehouseQuery;
import com.colorsteel.erp.warehouse.dto.WarehouseUpdateRequest;
import com.colorsteel.erp.warehouse.service.WarehouseService;
import com.colorsteel.erp.warehouse.vo.WarehouseVO;
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

@Tag(name = "仓库")
@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Operation(summary = "分页查询仓库")
    @GetMapping
    public Result<PageResult<WarehouseVO>> page(@Valid @ModelAttribute WarehouseQuery query) {
        return Result.ok(warehouseService.pageWarehouses(query));
    }

    @Operation(summary = "仓库详情")
    @GetMapping("/{id}")
    public Result<WarehouseVO> get(@PathVariable Long id) {
        return Result.ok(warehouseService.getWarehouse(id));
    }

    @Operation(summary = "新增仓库")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody WarehouseCreateRequest request) {
        return Result.ok(warehouseService.createWarehouse(request));
    }

    @Operation(summary = "编辑仓库")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody WarehouseUpdateRequest request) {
        warehouseService.updateWarehouse(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除仓库（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return Result.ok();
    }
}
