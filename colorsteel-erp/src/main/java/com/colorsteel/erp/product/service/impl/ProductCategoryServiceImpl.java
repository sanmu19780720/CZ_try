package com.colorsteel.erp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.product.dto.ProductCategoryCreateRequest;
import com.colorsteel.erp.product.dto.ProductCategoryQuery;
import com.colorsteel.erp.product.dto.ProductCategoryUpdateRequest;
import com.colorsteel.erp.product.entity.ProductCategory;
import com.colorsteel.erp.product.mapper.ProductCategoryMapper;
import com.colorsteel.erp.product.service.ProductCategoryService;
import com.colorsteel.erp.product.vo.ProductCategoryVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory>
        implements ProductCategoryService {

    @Override
    public PageResult<ProductCategoryVO> pageCategories(ProductCategoryQuery query) {
        Page<ProductCategory> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<ProductCategory> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getName()), ProductCategory::getName, query.getName());
        w.eq(query.getParentId() != null, ProductCategory::getParentId, query.getParentId());
        w.orderByAsc(ProductCategory::getSortOrder).orderByDesc(ProductCategory::getId);
        Page<ProductCategory> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(ProductCategoryVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public ProductCategoryVO getCategory(Long id) {
        ProductCategory e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return ProductCategoryVO.from(e);
    }

    @Override
    public Long createCategory(ProductCategoryCreateRequest request) {
        ProductCategory e = new ProductCategory();
        e.setParentId(request.getParentId());
        e.setName(request.getName());
        e.setSortOrder(request.getSortOrder());
        save(e);
        return e.getId();
    }

    @Override
    public void updateCategory(Long id, ProductCategoryUpdateRequest request) {
        ProductCategory e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        e.setParentId(request.getParentId());
        e.setName(request.getName());
        e.setSortOrder(request.getSortOrder());
        updateById(e);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }
}
