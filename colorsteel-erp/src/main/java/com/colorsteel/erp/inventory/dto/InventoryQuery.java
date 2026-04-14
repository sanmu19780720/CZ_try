package com.colorsteel.erp.inventory.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "库存分页查询")
public class InventoryQuery extends PageQuery {

    @Schema(description = "仓库ID")
    private Long warehouseId;

    @Schema(description = "商品ID")
    private Long productId;

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
