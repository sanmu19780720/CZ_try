package com.colorsteel.erp.payroll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.payroll.dto.PayrollCreateRequest;
import com.colorsteel.erp.payroll.dto.PayrollQuery;
import com.colorsteel.erp.payroll.entity.Payroll;
import com.colorsteel.erp.payroll.vo.PayrollVO;
import com.colorsteel.erp.project.vo.ProjectLaborCostVO;

import java.math.BigDecimal;

public interface PayrollService extends IService<Payroll> {

    Long createPayroll(PayrollCreateRequest request);

    PageResult<PayrollVO> pagePayrolls(PayrollQuery query);

    BigDecimal sumLaborCostByProject(Long projectId);

    ProjectLaborCostVO getProjectLaborCost(Long projectId);
}
