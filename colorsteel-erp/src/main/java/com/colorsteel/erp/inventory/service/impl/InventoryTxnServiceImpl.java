package com.colorsteel.erp.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.inventory.dto.InventoryTxnQuery;
import com.colorsteel.erp.inventory.entity.InventoryTxn;
import com.colorsteel.erp.inventory.mapper.InventoryTxnMapper;
import com.colorsteel.erp.inventory.service.InventoryTxnService;
import com.colorsteel.erp.inventory.vo.InventoryTxnVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class InventoryTxnServiceImpl extends ServiceImpl<InventoryTxnMapper, InventoryTxn> implements InventoryTxnService {

    @Override
    public PageResult<InventoryTxnVO> pageTxns(InventoryTxnQuery query) {
        Page<InventoryTxn> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<InventoryTxn> w = new LambdaQueryWrapper<>();
        w.eq(query.getWarehouseId() != null, InventoryTxn::getWarehouseId, query.getWarehouseId());
        w.eq(query.getProductId() != null, InventoryTxn::getProductId, query.getProductId());
        w.eq(StringUtils.hasText(query.getBizType()), InventoryTxn::getBizType, query.getBizType());
        w.eq(StringUtils.hasText(query.getDirection()), InventoryTxn::getDirection, query.getDirection());
        w.orderByDesc(InventoryTxn::getId);
        Page<InventoryTxn> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(InventoryTxnVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public InventoryTxnVO getTxn(Long id) {
        InventoryTxn e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return InventoryTxnVO.from(e);
    }
}
