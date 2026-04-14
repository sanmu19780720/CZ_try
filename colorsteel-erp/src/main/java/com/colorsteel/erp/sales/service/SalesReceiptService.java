package com.colorsteel.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.sales.dto.SalesReceiptCreateRequest;
import com.colorsteel.erp.sales.entity.SalesReceipt;
import com.colorsteel.erp.sales.vo.SalesReceiptVO;

import java.util.List;

public interface SalesReceiptService extends IService<SalesReceipt> {

    Long addReceipt(Long salesOrderId, SalesReceiptCreateRequest request);

    List<SalesReceiptVO> listBySalesOrder(Long salesOrderId);
}
