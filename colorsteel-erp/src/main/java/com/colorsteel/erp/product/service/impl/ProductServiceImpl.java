package com.colorsteel.erp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.product.dto.ProductCreateRequest;
import com.colorsteel.erp.product.dto.ProductQuery;
import com.colorsteel.erp.product.dto.ProductUpdateRequest;
import com.colorsteel.erp.product.entity.Product;
import com.colorsteel.erp.product.mapper.ProductMapper;
import com.colorsteel.erp.product.service.ProductService;
import com.colorsteel.erp.product.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public PageResult<ProductVO> pageProducts(ProductQuery query) {
        Page<Product> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getSku()), Product::getSku, query.getSku());
        w.like(StringUtils.hasText(query.getName()), Product::getName, query.getName());
        w.eq(query.getCategoryId() != null, Product::getCategoryId, query.getCategoryId());
        w.orderByDesc(Product::getId);
        Page<Product> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(ProductVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public ProductVO getProduct(Long id) {
        Product e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return ProductVO.from(e);
    }

    @Override
    public Long createProduct(ProductCreateRequest request) {
        Product e = new Product();
        e.setCategoryId(request.getCategoryId());
        e.setSku(request.getSku().trim());
        e.setName(request.getName());
        e.setUnit(StringUtils.hasText(request.getUnit()) ? request.getUnit() : "件");
        e.setSpec(request.getSpec());
        e.setStandardPrice(request.getStandardPrice());
        e.setRemark(request.getRemark());
        save(e);
        return e.getId();
    }

    @Override
    public void updateProduct(Long id, ProductUpdateRequest request) {
        Product e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        e.setCategoryId(request.getCategoryId());
        e.setName(request.getName());
        e.setUnit(request.getUnit());
        e.setSpec(request.getSpec());
        e.setStandardPrice(request.getStandardPrice());
        e.setRemark(request.getRemark());
        updateById(e);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }
}
