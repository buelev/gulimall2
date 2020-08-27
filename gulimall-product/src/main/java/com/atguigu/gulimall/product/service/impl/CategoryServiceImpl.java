package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entityList = baseMapper.selectList(null);
        //查询一级目录
        List<CategoryEntity> entities = entityList.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(category -> {
                    return getChildrenList(entityList, category);
                }).collect(Collectors.toList());
        return entities;
    }

    @Override
    public void removeMenuByIds(List<Long> ids) {
        //TODO 检查当前菜单是否被引用
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateCategory(CategoryEntity categoryEntity) {
        //更新分类维护
        this.updateById(categoryEntity);
        //更新品牌管理中的关联关系
        //查询关联的地方
//        categoryBrandRelationService.queryByCategoryId(categoryEntity.getCatId());
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setCatelogName(categoryEntity.getName());
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", categoryEntity.getCatId());
        categoryBrandRelationService.update(entity, wrapper);

    }

    private CategoryEntity getChildrenList(List<CategoryEntity> entityList, CategoryEntity category) {
        //查询一级目录下的二级目录
        List<CategoryEntity> secList = entityList.stream().filter(categoryEntity -> category.getCatId() == categoryEntity.getParentCid())
                .map(secCategory -> getChildrenList(entityList, secCategory))
                .sorted((category1, category2) -> {
                    return category1.getSort() - category2.getSort();
                })
                .collect(Collectors.toList());
        category.setChildrenList(secList);
        return category;
    }
}