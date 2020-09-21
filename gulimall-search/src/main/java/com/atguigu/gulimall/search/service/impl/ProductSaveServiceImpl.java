package com.atguigu.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.common.constant.ESConstant;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.search.service.ProductSaveService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * description: ProductSaveServiceImpl
 * date: 2020-09-21 17:39
 * author: buelev
 * version: 1.0
 */
@Service
public class ProductSaveServiceImpl implements ProductSaveService {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean saveProduct(List<SkuEsModel> esModelList) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        esModelList.stream().forEach(skuEsModel -> {
            IndexRequest request = new IndexRequest(ESConstant.PRODUCT_INDEX);
            request.id(skuEsModel.getSkuId().toString());
            String skuEsModelJSON = JSON.toJSONString(skuEsModel);
            request.source(skuEsModelJSON, XContentType.JSON);
            bulkRequest.add(request);
        });

        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        boolean b = bulkResponse.hasFailures();
        return b;
    }
}
