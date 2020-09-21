package com.atguigu.gulimall.product.feign;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * description: SearchFeignService
 * date: 2020-09-21 19:25
 * author: buelev
 * version: 1.0
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {
    String prefix_url = "search/save/";

    @PostMapping(prefix_url + "product")
    R productStateUp(List<SkuEsModel> esModelList);
}
