package com.colorsteel.erp.purchase.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.purchase.dto.PurchasePaymentCreateRequest;
import com.colorsteel.erp.purchase.service.PurchasePaymentService;
import com.colorsteel.erp.purchase.vo.PurchasePaymentVO;
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

@Tag(name = "采购付款")
@RestController
@RequestMapping("/api/v1/purchase-orders/{orderId}/payments")
public class PurchasePaymentController {

    private final PurchasePaymentService purchasePaymentService;

    public PurchasePaymentController(PurchasePaymentService purchasePaymentService) {
        this.purchasePaymentService = purchasePaymentService;
    }

    @Operation(summary = "登记付款（累加已付金额）")
    @PostMapping
    public Result<Long> add(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody PurchasePaymentCreateRequest request) {
        return Result.ok(purchasePaymentService.addPayment(orderId, request));
    }

    @Operation(summary = "付款记录列表")
    @GetMapping
    public Result<List<PurchasePaymentVO>> list(@PathVariable("orderId") Long orderId) {
        return Result.ok(purchasePaymentService.listByPurchaseOrder(orderId));
    }
}
