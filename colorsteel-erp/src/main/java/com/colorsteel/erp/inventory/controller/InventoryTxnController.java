package com.colorsteel.erp.inventory.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.inventory.dto.InventoryTxnQuery;
import com.colorsteel.erp.inventory.service.InventoryTxnService;
import com.colorsteel.erp.inventory.vo.InventoryTxnVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "库存流水")
@RestController
@RequestMapping("/api/v1/inventory-txns")
public class InventoryTxnController {

    private final InventoryTxnService inventoryTxnService;

    public InventoryTxnController(InventoryTxnService inventoryTxnService) {
        this.inventoryTxnService = inventoryTxnService;
    }

    @Operation(summary = "分页查询库存流水")
    @GetMapping
    public Result<PageResult<InventoryTxnVO>> page(@Valid @ModelAttribute InventoryTxnQuery query) {
        return Result.ok(inventoryTxnService.pageTxns(query));
    }

    @Operation(summary = "流水详情")
    @GetMapping("/{id}")
    public Result<InventoryTxnVO> get(@PathVariable Long id) {
        return Result.ok(inventoryTxnService.getTxn(id));
    }
}
