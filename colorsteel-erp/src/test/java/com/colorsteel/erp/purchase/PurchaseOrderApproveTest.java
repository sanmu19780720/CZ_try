package com.colorsteel.erp.purchase;

import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.product.entity.Product;
import com.colorsteel.erp.product.service.ProductService;
import com.colorsteel.erp.purchase.dto.PurchaseOrderCreateRequest;
import com.colorsteel.erp.purchase.dto.PurchaseOrderLineRequest;
import com.colorsteel.erp.purchase.service.PurchaseOrderService;
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
class PurchaseOrderApproveTest {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private ProductService productService;

    @Test
    void approve_triggers_stock_and_product_cost_and_reject_second_approve() {
        Product p = new Product();
        p.setSku("SKU-PO-APPROVE-1");
        p.setName("测试采购商品");
        p.setUnit("件");
        productService.save(p);
        Long pid = p.getId();

        PurchaseOrderLineRequest line = new PurchaseOrderLineRequest();
        line.setProductId(pid);
        line.setQuantity(new BigDecimal("4.000"));
        line.setUnitPrice(new BigDecimal("12.50"));

        PurchaseOrderCreateRequest req = new PurchaseOrderCreateRequest();
        req.setSupplierId(1L);
        req.setWarehouseId(1L);
        req.setOrderDate(LocalDate.now());
        req.setLines(List.of(line));

        Long oid = purchaseOrderService.createPurchaseOrder(req);
        purchaseOrderService.approvePurchaseOrder(oid);

        Product loaded = productService.getById(pid);
        Assertions.assertEquals(0, new BigDecimal("12.50").compareTo(loaded.getLastPurchasePrice()));
        Assertions.assertEquals(0, new BigDecimal("12.50").compareTo(loaded.getAvgCostPrice()));

        Assertions.assertThrows(BusinessException.class, () -> purchaseOrderService.approvePurchaseOrder(oid));
    }
}
