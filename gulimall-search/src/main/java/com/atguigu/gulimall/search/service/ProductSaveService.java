package com.atguigu.gulimall.search.service;

import com.atguigu.gulimall.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * description: ProductSaveService
 * date: 2020-09-21 17:38
 * author: buelev
 * version: 1.0
 */
public interface ProductSaveService {
    /**
     * 商品上架服务
     * @param esModelList
     * @return
     */
    Boolean saveProduct(List<SkuEsModel> esModelList) throws IOException;
}
