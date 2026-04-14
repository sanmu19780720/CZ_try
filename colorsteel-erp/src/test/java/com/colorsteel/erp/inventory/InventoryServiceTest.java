package com.colorsteel.erp.inventory;

import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.inventory.service.InventoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
class InventoryServiceTest {

    private static final Long WH = 1L;
    private static final Long PID = 1L;

    @Autowired
    private InventoryService inventoryService;

    @Test
    void addThenDeduct_updatesAvgAndTxn() {
        inventoryService.addStock(PID, WH, new BigDecimal("10.000"), new BigDecimal("5.00"),
                "PURCHASE_IN", 100L);
        inventoryService.addStock(PID, WH, new BigDecimal("10.000"), new BigDecimal("7.00"),
                "PURCHASE_IN", 101L);

        var inv = inventoryService.lambdaQuery()
                .eq(com.colorsteel.erp.inventory.entity.Inventory::getWarehouseId, WH)
                .eq(com.colorsteel.erp.inventory.entity.Inventory::getProductId, PID)
                .one();
        Assertions.assertNotNull(inv);
        Assertions.assertEquals(0, new BigDecimal("20.000").compareTo(inv.getQuantity()));
        Assertions.assertEquals(0, new BigDecimal("6.00").compareTo(inv.getAvgUnitCost()));

        inventoryService.deductStock(PID, WH, new BigDecimal("5.000"), "SALES_OUT", 200L);
        inv = inventoryService.getById(inv.getId());
        Assertions.assertEquals(0, new BigDecimal("15.000").compareTo(inv.getQuantity()));
    }

    @Test
    void deduct_insufficient_throws() {
        inventoryService.addStock(PID, WH, new BigDecimal("1.000"), new BigDecimal("1.00"),
                "PURCHASE_IN", 1L);
        Assertions.assertThrows(BusinessException.class,
                () -> inventoryService.deductStock(PID, WH, new BigDecimal("100.000"), "SALES_OUT", 2L));
    }
}
