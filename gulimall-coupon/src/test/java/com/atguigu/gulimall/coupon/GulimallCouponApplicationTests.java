package com.atguigu.gulimall.coupon;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.coupon.feign.OpenFeigenService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class GulimallCouponApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private OpenFeigenService openFeigenService;

    @Test
    public void list() {
        R list = openFeigenService.list(new HashMap<>());
        System.out.println(list);
    }
}
