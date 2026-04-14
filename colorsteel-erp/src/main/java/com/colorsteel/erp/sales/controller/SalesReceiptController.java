package com.colorsteel.erp.sales.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.sales.dto.SalesReceiptCreateRequest;
import com.colorsteel.erp.sales.service.SalesReceiptService;
import com.colorsteel.erp.sales.vo.SalesReceiptVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "销售收款")
@RestController
@RequestMapping("/api/v1/sales-orders/{orderId}/receipts")
public class SalesReceiptController {

    private final SalesReceiptService salesReceiptService;

    public SalesReceiptController(SalesReceiptService salesReceiptService) {
        this.salesReceiptService = salesReceiptService;
    }

    @Operation(summary = "登记收款")
    @PostMapping
    public Result<Long> add(@PathVariable Long orderId, @Valid @RequestBody SalesReceiptCreateRequest request) {
        return Result.ok(salesReceiptService.addReceipt(orderId, request));
    }

    @Operation(summary = "收款列表")
    @GetMapping
    public Result<List<SalesReceiptVO>> list(@PathVariable Long orderId) {
        return Result.ok(salesReceiptService.listBySalesOrder(orderId));
    }
}
