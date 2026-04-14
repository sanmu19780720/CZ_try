package com.colorsteel.erp.supplier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.supplier.dto.SupplierCreateRequest;
import com.colorsteel.erp.supplier.dto.SupplierQuery;
import com.colorsteel.erp.supplier.dto.SupplierUpdateRequest;
import com.colorsteel.erp.supplier.entity.Supplier;
import com.colorsteel.erp.supplier.vo.SupplierVO;

public interface SupplierService extends IService<Supplier> {

    PageResult<SupplierVO> pageSuppliers(SupplierQuery query);

    SupplierVO getSupplier(Long id);

    Long createSupplier(SupplierCreateRequest request);

    void updateSupplier(Long id, SupplierUpdateRequest request);

    void deleteSupplier(Long id);
}
