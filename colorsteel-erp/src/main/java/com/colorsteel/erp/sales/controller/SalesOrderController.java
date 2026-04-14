package com.colorsteel.erp.sales.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.sales.dto.SalesOrderCreateRequest;
import com.colorsteel.erp.sales.dto.SalesOrderQuery;
import com.colorsteel.erp.sales.service.SalesOrderService;
import com.colorsteel.erp.sales.vo.SalesOrderDetailVO;
import com.colorsteel.erp.sales.vo.SalesOrderSummaryVO;
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

@Tag(name = "销售单")
@RestController
@RequestMapping("/api/v1/sales-orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @Operation(summary = "分页查询销售单")
    @GetMapping
    public Result<PageResult<SalesOrderSummaryVO>> page(@Valid @ModelAttribute SalesOrderQuery query) {
        return Result.ok(salesOrderService.pageSalesOrders(query));
    }

    @Operation(summary = "销售单详情（含明细）")
    @GetMapping("/{id}")
    public Result<SalesOrderDetailVO> detail(@PathVariable Long id) {
        return Result.ok(salesOrderService.getSalesOrderDetail(id));
    }

    @Operation(summary = "创建销售单（草稿，不出库）")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody SalesOrderCreateRequest request) {
        return Result.ok(salesOrderService.createSalesOrder(request));
    }

    @Operation(summary = "审核销售单（出库 + 成本固化 + 毛利）")
    @PostMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        salesOrderService.approveSalesOrder(id);
        return Result.ok();
    }

    @Operation(summary = "作废草稿销售单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        salesOrderService.cancelSalesOrder(id);
        return Result.ok();
    }
}
