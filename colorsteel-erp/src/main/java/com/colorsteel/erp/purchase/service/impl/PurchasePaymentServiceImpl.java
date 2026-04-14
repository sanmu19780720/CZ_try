package com.colorsteel.erp.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.purchase.dto.PurchasePaymentCreateRequest;
import com.colorsteel.erp.purchase.entity.PurchaseOrder;
import com.colorsteel.erp.purchase.entity.PurchasePayment;
import com.colorsteel.erp.purchase.mapper.PurchaseOrderMapper;
import com.colorsteel.erp.purchase.mapper.PurchasePaymentMapper;
import com.colorsteel.erp.purchase.service.PurchasePaymentService;
import com.colorsteel.erp.purchase.support.PurchaseOrderStatus;
import com.colorsteel.erp.purchase.support.PurchasePaymentNoGenerator;
import com.colorsteel.erp.purchase.vo.PurchasePaymentVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchasePaymentServiceImpl extends ServiceImpl<PurchasePaymentMapper, PurchasePayment>
        implements PurchasePaymentService {

    private static final int MONEY_SCALE = 2;

    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchasePaymentServiceImpl(PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    private PurchaseOrder lockOrder(Long orderId) {
        LambdaQueryWrapper<PurchaseOrder> w = new LambdaQueryWrapper<>();
        w.eq(PurchaseOrder::getId, orderId).last("FOR UPDATE");
        return purchaseOrderMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addPayment(Long purchaseOrderId, PurchasePaymentCreateRequest request) {
        PurchaseOrder order = lockOrder(purchaseOrderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (PurchaseOrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "已作废采购单不可付款");
        }

        PurchasePayment p = new PurchasePayment();
        p.setPaymentNo(PurchasePaymentNoGenerator.next());
        p.setPurchaseOrderId(purchaseOrderId);
        p.setPayDate(request.getPayDate());
        p.setAmount(request.getAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        p.setPayMethod(request.getPayMethod());
        p.setRemark(request.getRemark());
        save(p);

        BigDecimal paid = order.getPaidAmount() == null ? BigDecimal.ZERO : order.getPaidAmount();
        order.setPaidAmount(paid.add(p.getAmount()).setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        purchaseOrderMapper.updateById(order);
        return p.getId();
    }

    @Override
    public List<PurchasePaymentVO> listByPurchaseOrder(Long purchaseOrderId) {
        List<PurchasePayment> list = lambdaQuery()
                .eq(PurchasePayment::getPurchaseOrderId, purchaseOrderId)
                .orderByDesc(PurchasePayment::getId)
                .list();
        return list.stream().map(PurchasePaymentVO::from).collect(Collectors.toList());
    }
}
