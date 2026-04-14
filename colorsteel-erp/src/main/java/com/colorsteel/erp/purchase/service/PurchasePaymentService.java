package com.colorsteel.erp.purchase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.purchase.dto.PurchasePaymentCreateRequest;
import com.colorsteel.erp.purchase.entity.PurchasePayment;
import com.colorsteel.erp.purchase.vo.PurchasePaymentVO;

import java.util.List;

public interface PurchasePaymentService extends IService<PurchasePayment> {

    Long addPayment(Long purchaseOrderId, PurchasePaymentCreateRequest request);

    List<PurchasePaymentVO> listByPurchaseOrder(Long purchaseOrderId);
}
