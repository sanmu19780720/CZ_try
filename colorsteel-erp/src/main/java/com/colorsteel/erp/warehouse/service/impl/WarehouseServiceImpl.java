package com.colorsteel.erp.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.warehouse.dto.WarehouseCreateRequest;
import com.colorsteel.erp.warehouse.dto.WarehouseQuery;
import com.colorsteel.erp.warehouse.dto.WarehouseUpdateRequest;
import com.colorsteel.erp.warehouse.entity.Warehouse;
import com.colorsteel.erp.warehouse.mapper.WarehouseMapper;
import com.colorsteel.erp.warehouse.service.WarehouseService;
import com.colorsteel.erp.warehouse.vo.WarehouseVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    @Override
    public PageResult<WarehouseVO> pageWarehouses(WarehouseQuery query) {
        Page<Warehouse> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<Warehouse> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getWarehouseCode()), Warehouse::getWarehouseCode, query.getWarehouseCode());
        w.like(StringUtils.hasText(query.getName()), Warehouse::getName, query.getName());
        w.orderByDesc(Warehouse::getId);
        Page<Warehouse> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(WarehouseVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public WarehouseVO getWarehouse(Long id) {
        Warehouse e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return WarehouseVO.from(e);
    }

    @Override
    public Long createWarehouse(WarehouseCreateRequest request) {
        Warehouse e = new Warehouse();
        e.setWarehouseCode(request.getWarehouseCode().trim());
        e.setName(request.getName());
        e.setAddress(request.getAddress());
        save(e);
        return e.getId();
    }

    @Override
    public void updateWarehouse(Long id, WarehouseUpdateRequest request) {
        Warehouse e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        e.setName(request.getName());
        e.setAddress(request.getAddress());
        updateById(e);
    }

    @Override
    public void deleteWarehouse(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }
}
