package com.atguigu.gulimall.product.controller;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * description: CategoryController
 * date: 2020-07-27 16:02
 * author: buelev
 * version: 1.0
 */
@RestController
@RequestMapping("product/category")
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
    /**
     * 删除节点的操作
     */
    @PostMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
//        categoryService.removeByIds(Arrays.asList(ids));
        categoryService.removeMenuByIds(Arrays.asList(ids));
        return R.ok();
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryEntity categoryEntity){
        categoryService.save(categoryEntity);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryEntity categoryEntity){
        categoryService.updateById(categoryEntity);
        return R.ok();
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
        CategoryEntity categoryEntity = categoryService.getById(id);

        return R.ok().put("data", categoryEntity);
    }
}
