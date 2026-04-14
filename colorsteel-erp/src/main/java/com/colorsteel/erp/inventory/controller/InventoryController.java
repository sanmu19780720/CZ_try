package com.colorsteel.erp.inventory.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.inventory.dto.InventoryCreateRequest;
import com.colorsteel.erp.inventory.dto.InventoryQuery;
import com.colorsteel.erp.inventory.dto.InventoryUpdateRequest;
import com.colorsteel.erp.inventory.dto.StockInRequest;
import com.colorsteel.erp.inventory.dto.StockOutRequest;
import com.colorsteel.erp.inventory.service.InventoryService;
import com.colorsteel.erp.inventory.vo.InventoryVO;
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

@Tag(name = "库存")
@RestController
@RequestMapping("/api/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "分页查询当前库存")
    @GetMapping
    public Result<PageResult<InventoryVO>> page(@Valid @ModelAttribute InventoryQuery query) {
        return Result.ok(inventoryService.pageInventories(query));
    }

    @Operation(summary = "库存行详情")
    @GetMapping("/{id}")
    public Result<InventoryVO> get(@PathVariable Long id) {
        return Result.ok(inventoryService.getInventory(id));
    }

    @Operation(summary = "手工新增库存行（不记流水，慎用）")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody InventoryCreateRequest request) {
        return Result.ok(inventoryService.createInventory(request));
    }

    @Operation(summary = "手工更新库存行（不记流水，慎用）")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody InventoryUpdateRequest request) {
        inventoryService.updateInventory(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除库存行（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return Result.ok();
    }

    @Operation(summary = "入库（更新结存 + 写流水）")
    @PostMapping("/stock-in")
    public Result<Void> stockIn(@Valid @RequestBody StockInRequest request) {
        inventoryService.addStock(
                request.getProductId(),
                request.getWarehouseId(),
                request.getQuantity(),
                request.getCostPrice(),
                request.getBizType(),
                request.getBizId()
        );
        return Result.ok();
    }

    @Operation(summary = "出库（校验库存 + 更新结存 + 写流水）")
    @PostMapping("/stock-out")
    public Result<Void> stockOut(@Valid @RequestBody StockOutRequest request) {
        inventoryService.deductStock(
                request.getProductId(),
                request.getWarehouseId(),
                request.getQuantity(),
                request.getBizType(),
                request.getBizId()
        );
        return Result.ok();
    }
}
