package com.atguigu.gulimall.product.controller;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description: CategoryController
 * date: 2020-07-27 16:02
 * author: buelev
 * version: 1.0
 */
@RestController
@RequestMapping("product/categotry")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询商品分类数据，以树形结构展示
     * @return
     */
    @RequestMapping("list")
    public R list() {
        List<CategoryEntity> entries = categoryService.listWithTree();
        return R.ok().put("page", entries);
    }
}
