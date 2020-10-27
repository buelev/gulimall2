package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.search.vo.SearchParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * description: IndexController
 * date: 2020-10-23 14:42
 * author: buelev
 * version: 1.0
 */
@Controller
@RequestMapping
public class IndexController {

    @GetMapping("list.html")
    public String toIndex(SearchParam searchParam) {
        return "index";
    }
}
