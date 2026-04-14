package com.colorsteel.erp.customer.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.customer.dto.CustomerCreateRequest;
import com.colorsteel.erp.customer.dto.CustomerQuery;
import com.colorsteel.erp.customer.dto.CustomerUpdateRequest;
import com.colorsteel.erp.customer.service.CustomerService;
import com.colorsteel.erp.customer.vo.CustomerVO;
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

@Tag(name = "客户")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "分页查询客户")
    @GetMapping
    public Result<PageResult<CustomerVO>> page(@Valid @ModelAttribute CustomerQuery query) {
        return Result.ok(customerService.pageCustomers(query));
    }

    @Operation(summary = "客户详情")
    @GetMapping("/{id}")
    public Result<CustomerVO> get(@PathVariable Long id) {
        return Result.ok(customerService.getCustomer(id));
    }

    @Operation(summary = "新增客户")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody CustomerCreateRequest request) {
        return Result.ok(customerService.createCustomer(request));
    }

    @Operation(summary = "编辑客户")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequest request) {
        customerService.updateCustomer(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除客户（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return Result.ok();
    }
}
