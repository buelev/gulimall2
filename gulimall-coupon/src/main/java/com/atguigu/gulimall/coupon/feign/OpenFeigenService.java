package com.atguigu.gulimall.coupon.feign;

import com.atguigu.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * description: OpenFeigenService
 * date: 2020-07-09 00:04
 * author: buelev
 * version: 1.0
 */
@FeignClient(name = "gulimall-member")
public interface OpenFeigenService {
    String prefix_url = "member/member/";

    @RequestMapping(prefix_url + "list")
    R list(Map<String, Object> map);
}
