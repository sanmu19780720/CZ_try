package com.colorsteel.erp.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.project.dto.ProjectReceiptCreateRequest;
import com.colorsteel.erp.project.entity.ProjectReceipt;
import com.colorsteel.erp.project.vo.ProjectReceiptVO;

import java.util.List;

public interface ProjectReceiptService extends IService<ProjectReceipt> {

    Long addReceipt(Long projectId, ProjectReceiptCreateRequest request);

    List<ProjectReceiptVO> listByProject(Long projectId);
}
