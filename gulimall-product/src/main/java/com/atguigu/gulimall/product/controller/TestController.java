package com.atguigu.gulimall.product.controller;

import com.atguigu.gulimall.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: TestController
 * date: 2020-07-15 14:46
 * author: buelev
 * version: 1.0
 */
@RestController
@RequestMapping("nacos")
@RefreshScope
public class TestController {

//    @Value("${user.name}")
//    private String userName;
//    @Value("${user.age}")
//    private Integer age;
//
//    @RequestMapping("config/test")
//    public R testConfig() {
//        return R.ok().put("name", userName).put("age", age);
//    }
}
