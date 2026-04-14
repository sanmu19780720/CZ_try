package com.colorsteel.erp.purchase.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.purchase.dto.PurchaseOrderCreateRequest;
import com.colorsteel.erp.purchase.dto.PurchaseOrderQuery;
import com.colorsteel.erp.purchase.service.PurchaseOrderService;
import com.colorsteel.erp.purchase.vo.PurchaseOrderDetailVO;
import com.colorsteel.erp.purchase.vo.PurchaseOrderSummaryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "采购单")
@RestController
@RequestMapping("/api/v1/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @Operation(summary = "分页查询采购单")
    @GetMapping
    public Result<PageResult<PurchaseOrderSummaryVO>> page(@Valid @ModelAttribute PurchaseOrderQuery query) {
        return Result.ok(purchaseOrderService.pagePurchaseOrders(query));
    }

    @Operation(summary = "采购单详情（含明细）")
    @GetMapping("/{id}")
    public Result<PurchaseOrderDetailVO> detail(@PathVariable Long id) {
        return Result.ok(purchaseOrderService.getPurchaseOrderDetail(id));
    }

    @Operation(summary = "创建采购单（草稿，不占用库存）")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody PurchaseOrderCreateRequest request) {
        return Result.ok(purchaseOrderService.createPurchaseOrder(request));
    }

    @Operation(summary = "审核采购单（入库 + 更新商品成本价 + 流水）")
    @PostMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        purchaseOrderService.approvePurchaseOrder(id);
        return Result.ok();
    }

    @Operation(summary = "作废草稿采购单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        purchaseOrderService.cancelPurchaseOrder(id);
        return Result.ok();
    }
}
