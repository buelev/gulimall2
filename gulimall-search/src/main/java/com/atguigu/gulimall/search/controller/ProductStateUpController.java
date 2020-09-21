package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.common.exception.BizCodeEnume;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * description: ProductStateUp
 * date: 2020-09-21 17:33
 * author: buelev
 * version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("search/save")
public class ProductStateUpController {
    @Resource
    private ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStateUp(@RequestBody List<SkuEsModel> esModelList) {
        Boolean success = false;
        try {
            success = productSaveService.saveProduct(esModelList);
        } catch (IOException e) {
            log.error("商品上架出错,异常信息为:{}", e);
        }
        if (success) {
            return R.ok();
        }
        return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
    }
}
