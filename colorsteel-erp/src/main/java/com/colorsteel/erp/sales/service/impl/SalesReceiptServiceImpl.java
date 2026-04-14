package com.colorsteel.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.sales.dto.SalesReceiptCreateRequest;
import com.colorsteel.erp.sales.entity.SalesOrder;
import com.colorsteel.erp.sales.entity.SalesReceipt;
import com.colorsteel.erp.sales.mapper.SalesOrderMapper;
import com.colorsteel.erp.sales.mapper.SalesReceiptMapper;
import com.colorsteel.erp.sales.service.SalesReceiptService;
import com.colorsteel.erp.sales.support.SalesOrderStatus;
import com.colorsteel.erp.sales.support.SalesReceiptNoGenerator;
import com.colorsteel.erp.sales.vo.SalesReceiptVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesReceiptServiceImpl extends ServiceImpl<SalesReceiptMapper, SalesReceipt>
        implements SalesReceiptService {

    private static final int MONEY_SCALE = 2;

    private final SalesOrderMapper salesOrderMapper;

    public SalesReceiptServiceImpl(SalesOrderMapper salesOrderMapper) {
        this.salesOrderMapper = salesOrderMapper;
    }

    private SalesOrder lockOrder(Long orderId) {
        LambdaQueryWrapper<SalesOrder> w = new LambdaQueryWrapper<>();
        w.eq(SalesOrder::getId, orderId).last("FOR UPDATE");
        return salesOrderMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addReceipt(Long salesOrderId, SalesReceiptCreateRequest request) {
        SalesOrder order = lockOrder(salesOrderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (SalesOrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "已作废销售单不可收款");
        }

        SalesReceipt r = new SalesReceipt();
        r.setReceiptNo(SalesReceiptNoGenerator.next());
        r.setSalesOrderId(salesOrderId);
        r.setReceiptDate(request.getReceiptDate());
        r.setAmount(request.getAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        r.setPayMethod(request.getPayMethod());
        r.setRemark(request.getRemark());
        save(r);

        BigDecimal received = order.getReceivedAmount() == null ? BigDecimal.ZERO : order.getReceivedAmount();
        order.setReceivedAmount(received.add(r.getAmount()).setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        salesOrderMapper.updateById(order);
        return r.getId();
    }

    @Override
    public List<SalesReceiptVO> listBySalesOrder(Long salesOrderId) {
        List<SalesReceipt> list = lambdaQuery()
                .eq(SalesReceipt::getSalesOrderId, salesOrderId)
                .orderByDesc(SalesReceipt::getId)
                .list();
        return list.stream().map(SalesReceiptVO::from).collect(Collectors.toList());
    }
}
