package com.colorsteel.erp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.product.dto.ProductCategoryCreateRequest;
import com.colorsteel.erp.product.dto.ProductCategoryQuery;
import com.colorsteel.erp.product.dto.ProductCategoryUpdateRequest;
import com.colorsteel.erp.product.entity.ProductCategory;
import com.colorsteel.erp.product.vo.ProductCategoryVO;

public interface ProductCategoryService extends IService<ProductCategory> {

    PageResult<ProductCategoryVO> pageCategories(ProductCategoryQuery query);

    ProductCategoryVO getCategory(Long id);

    Long createCategory(ProductCategoryCreateRequest request);

    void updateCategory(Long id, ProductCategoryUpdateRequest request);

    void deleteCategory(Long id);
}
