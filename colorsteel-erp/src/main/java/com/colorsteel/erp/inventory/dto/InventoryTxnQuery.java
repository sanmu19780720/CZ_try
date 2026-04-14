package com.colorsteel.erp.inventory.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "库存流水分页查询")
public class InventoryTxnQuery extends PageQuery {

    @Schema(description = "仓库ID")
    private Long warehouseId;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "方向 IN / OUT")
    private String direction;

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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
