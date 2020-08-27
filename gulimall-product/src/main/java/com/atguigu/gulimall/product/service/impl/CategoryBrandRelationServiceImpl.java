package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", (String) params.get("brandId"));
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void saveRelation(CategoryBrandRelationEntity categoryBrandRelation) {
        BrandEntity brandEntity = brandService.getById(categoryBrandRelation.getBrandId());
        CategoryEntity categoryEntity = categoryService.getById(categoryBrandRelation.getCatelogId());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

}