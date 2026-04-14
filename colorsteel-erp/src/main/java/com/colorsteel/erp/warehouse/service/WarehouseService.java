package com.colorsteel.erp.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.warehouse.dto.WarehouseCreateRequest;
import com.colorsteel.erp.warehouse.dto.WarehouseQuery;
import com.colorsteel.erp.warehouse.dto.WarehouseUpdateRequest;
import com.colorsteel.erp.warehouse.entity.Warehouse;
import com.colorsteel.erp.warehouse.vo.WarehouseVO;

public interface WarehouseService extends IService<Warehouse> {

    PageResult<WarehouseVO> pageWarehouses(WarehouseQuery query);

    WarehouseVO getWarehouse(Long id);

    Long createWarehouse(WarehouseCreateRequest request);

    void updateWarehouse(Long id, WarehouseUpdateRequest request);

    void deleteWarehouse(Long id);
}
