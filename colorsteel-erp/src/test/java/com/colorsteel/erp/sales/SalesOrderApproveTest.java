package com.colorsteel.erp.sales;

import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.customer.entity.Customer;
import com.colorsteel.erp.customer.service.CustomerService;
import com.colorsteel.erp.inventory.service.InventoryService;
import com.colorsteel.erp.product.entity.Product;
import com.colorsteel.erp.product.service.ProductService;
import com.colorsteel.erp.sales.dto.SalesOrderCreateRequest;
import com.colorsteel.erp.sales.dto.SalesOrderLineRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.colorsteel.erp.sales.entity.SalesOrderItem;
import com.colorsteel.erp.sales.mapper.SalesOrderItemMapper;
import com.colorsteel.erp.sales.service.SalesOrderService;
import com.colorsteel.erp.sales.support.SalesOrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class SalesOrderApproveTest {

    private static final Long WH = 1L;

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private SalesOrderItemMapper salesOrderItemMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private CustomerService customerService;

    @Test
    void approve_deducts_stock_and_freezes_cost_and_gross_profit_and_auto_customer() {
        Product p = new Product();
        p.setSku("SKU-SO-APPROVE-1");
        p.setName("销售测试商品");
        p.setUnit("件");
        productService.save(p);
        Long pid = p.getId();

        inventoryService.addStock(pid, WH, new BigDecimal("10.000"), new BigDecimal("5.00"),
                "PURCHASE_IN", 9001L);

        SalesOrderLineRequest line = new SalesOrderLineRequest();
        line.setProductId(pid);
        line.setQuantity(new BigDecimal("4.000"));
        line.setUnitPrice(new BigDecimal("20.00"));

        SalesOrderCreateRequest req = new SalesOrderCreateRequest();
        req.setCustomerName("临时客户甲");
        req.setCustomerPhone("13900000001");
        req.setWarehouseId(WH);
        req.setOrderDate(LocalDate.now());
        req.setLines(List.of(line));

        Long oid = salesOrderService.createSalesOrder(req);
        Customer any = customerService.lambdaQuery().eq(Customer::getName, "临时客户甲").one();
        Assertions.assertNotNull(any);

        salesOrderService.approveSalesOrder(oid);

        SalesOrderItem item = salesOrderItemMapper.selectList(
                new LambdaQueryWrapper<SalesOrderItem>()
                        .eq(SalesOrderItem::getSalesOrderId, oid)).get(0);
        Assertions.assertEquals(0, new BigDecimal("5.00").compareTo(item.getUnitCost()));
        Assertions.assertEquals(0, new BigDecimal("20.00").compareTo(item.getCostAmount()));
        Assertions.assertEquals(0, new BigDecimal("60.00").compareTo(item.getGrossProfit()));

        var order = salesOrderService.getById(oid);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(SalesOrderStatus.APPROVED, order.getStatus());
        Assertions.assertEquals(0, new BigDecimal("20.00").compareTo(order.getTotalCostAmount()));
        Assertions.assertEquals(0, new BigDecimal("60.00").compareTo(order.getGrossProfit()));

        Assertions.assertThrows(BusinessException.class, () -> salesOrderService.approveSalesOrder(oid));
    }
}
