package com.colorsteel.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.sales.dto.SalesOrderCreateRequest;
import com.colorsteel.erp.sales.dto.SalesOrderQuery;
import com.colorsteel.erp.sales.entity.SalesOrder;
import com.colorsteel.erp.sales.vo.SalesOrderDetailVO;
import com.colorsteel.erp.sales.vo.SalesOrderSummaryVO;

public interface SalesOrderService extends IService<SalesOrder> {

    Long createSalesOrder(SalesOrderCreateRequest request);

    void approveSalesOrder(Long orderId);

    void cancelSalesOrder(Long orderId);

    SalesOrderDetailVO getSalesOrderDetail(Long orderId);

    PageResult<SalesOrderSummaryVO> pageSalesOrders(SalesOrderQuery query);
}
