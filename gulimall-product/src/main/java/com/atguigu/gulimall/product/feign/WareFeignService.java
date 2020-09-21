package com.atguigu.gulimall.product.feign;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.product.to.SkuHasStock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * description: WareFeignService
 * date: 2020-09-21 16:38
 * author: buelev
 * version: 1.0
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping("ware/waresku/hasstock")
    R<List<SkuHasStock>> hasStock(List<Long> skuIds);
}
