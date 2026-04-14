package com.colorsteel.erp.supplier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.supplier.dto.SupplierCreateRequest;
import com.colorsteel.erp.supplier.dto.SupplierQuery;
import com.colorsteel.erp.supplier.dto.SupplierUpdateRequest;
import com.colorsteel.erp.supplier.entity.Supplier;
import com.colorsteel.erp.supplier.mapper.SupplierMapper;
import com.colorsteel.erp.supplier.service.SupplierService;
import com.colorsteel.erp.supplier.vo.SupplierVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Override
    public PageResult<SupplierVO> pageSuppliers(SupplierQuery query) {
        Page<Supplier> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<Supplier> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getSupplierNo()), Supplier::getSupplierNo, query.getSupplierNo());
        w.like(StringUtils.hasText(query.getName()), Supplier::getName, query.getName());
        w.orderByDesc(Supplier::getId);
        Page<Supplier> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(SupplierVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public SupplierVO getSupplier(Long id) {
        Supplier e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return SupplierVO.from(e);
    }

    @Override
    public Long createSupplier(SupplierCreateRequest request) {
        Supplier e = new Supplier();
        e.setSupplierNo(request.getSupplierNo().trim());
        e.setName(request.getName());
        e.setContactName(request.getContactName());
        e.setPhone(request.getPhone());
        e.setAddress(request.getAddress());
        e.setRemark(request.getRemark());
        save(e);
        return e.getId();
    }

    @Override
    public void updateSupplier(Long id, SupplierUpdateRequest request) {
        Supplier e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        e.setName(request.getName());
        e.setContactName(request.getContactName());
        e.setPhone(request.getPhone());
        e.setAddress(request.getAddress());
        e.setRemark(request.getRemark());
        updateById(e);
    }

    @Override
    public void deleteSupplier(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }
}
