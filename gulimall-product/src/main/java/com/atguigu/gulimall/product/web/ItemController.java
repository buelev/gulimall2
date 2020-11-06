package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.service.SkuInfoService;
import com.atguigu.gulimall.product.vo.SkuItemVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * description: ItemController
 * date: 2020-11-04 15:17
 * author: buelev
 * version: 1.0
 */
@Controller
public class ItemController {
    @Resource
    private SkuInfoService skuInfoService;

    @GetMapping("{skuId}.html")
    public String toItemHtml(@PathVariable("skuId") Long skuId, Model model) {
        //查询商品详情
        SkuItemVO skuItemVO = skuInfoService.queryItem(skuId);
        model.addAttribute("item", skuItemVO);
        return "item";
    }
}
