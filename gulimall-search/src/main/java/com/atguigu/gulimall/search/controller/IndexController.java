package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.search.service.MallSearchService;
import com.atguigu.gulimall.search.vo.GuliMallSearchResponse;
import com.atguigu.gulimall.search.vo.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * description: IndexController
 * date: 2020-10-23 14:42
 * author: buelev
 * version: 1.0
 */
@Controller
@RequestMapping
public class IndexController {
    @Resource
    private MallSearchService searchService;
    @GetMapping("list.html")
    public String toIndex(SearchParam searchParam) {
        GuliMallSearchResponse searchResponse = searchService.search(searchParam);
        return "index";
    }
}
