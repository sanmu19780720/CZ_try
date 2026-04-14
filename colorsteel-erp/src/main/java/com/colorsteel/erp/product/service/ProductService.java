package com.colorsteel.erp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.product.dto.ProductCreateRequest;
import com.colorsteel.erp.product.dto.ProductQuery;
import com.colorsteel.erp.product.dto.ProductUpdateRequest;
import com.colorsteel.erp.product.entity.Product;
import com.colorsteel.erp.product.vo.ProductVO;

public interface ProductService extends IService<Product> {

    PageResult<ProductVO> pageProducts(ProductQuery query);

    ProductVO getProduct(Long id);

    Long createProduct(ProductCreateRequest request);

    void updateProduct(Long id, ProductUpdateRequest request);

    void deleteProduct(Long id);
}
