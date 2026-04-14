package com.colorsteel.erp.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.customer.dto.CustomerCreateRequest;
import com.colorsteel.erp.customer.dto.CustomerQuery;
import com.colorsteel.erp.customer.dto.CustomerUpdateRequest;
import com.colorsteel.erp.customer.entity.Customer;
import com.colorsteel.erp.customer.mapper.CustomerMapper;
import com.colorsteel.erp.customer.service.CustomerService;
import com.colorsteel.erp.customer.vo.CustomerVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Override
    public PageResult<CustomerVO> pageCustomers(CustomerQuery query) {
        Page<Customer> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<Customer> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getCustomerNo()), Customer::getCustomerNo, query.getCustomerNo());
        w.like(StringUtils.hasText(query.getName()), Customer::getName, query.getName());
        w.orderByDesc(Customer::getId);
        Page<Customer> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(CustomerVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public CustomerVO getCustomer(Long id) {
        Customer e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return CustomerVO.from(e);
    }

    @Override
    public Long createCustomer(CustomerCreateRequest request) {
        Customer e = new Customer();
        e.setCustomerNo(request.getCustomerNo().trim());
        e.setName(request.getName());
        e.setContactName(request.getContactName());
        e.setPhone(request.getPhone());
        e.setAddress(request.getAddress());
        e.setRemark(request.getRemark());
        save(e);
        return e.getId();
    }

    @Override
    public void updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer e = getById(id);
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
    public void deleteCustomer(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }
}
