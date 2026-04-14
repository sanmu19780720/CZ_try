package com.colorsteel.erp.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.customer.dto.CustomerCreateRequest;
import com.colorsteel.erp.customer.dto.CustomerQuery;
import com.colorsteel.erp.customer.dto.CustomerUpdateRequest;
import com.colorsteel.erp.customer.entity.Customer;
import com.colorsteel.erp.customer.vo.CustomerVO;

public interface CustomerService extends IService<Customer> {

    PageResult<CustomerVO> pageCustomers(CustomerQuery query);

    CustomerVO getCustomer(Long id);

    Long createCustomer(CustomerCreateRequest request);

    void updateCustomer(Long id, CustomerUpdateRequest request);

    void deleteCustomer(Long id);
}
