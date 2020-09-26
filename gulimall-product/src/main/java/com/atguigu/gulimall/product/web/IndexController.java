package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.IndexCategoryVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * description: IndexController
 * date: 2020-09-22 10:51
 * author: buelev
 * version: 1.0
 */
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //查询以及分类的数据
        List<CategoryEntity> list = categoryService.indexCategory();
        model.addAttribute("indexCategories", list);
        return "index";
    }

    /**
     * 查询二级和三级分类数据
     */
    @GetMapping("index/json/catalog.json")
    @ResponseBody
    public Map<String, List<IndexCategoryVO>> getCategoryJson() {
        return categoryService.getSecondCategoryJson();
    }

}
