package com.atguigu.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * description: 搜索参数拼装
 * date: 2020-10-23 15:49
 * author: buelev
 * version: 1.0
 */
@Data
public class SearchParam {
    private String keyword;
    private List<Long> brandId;//品牌多选
    private Long catalogId;
    private String sort;//排序条件
    private Integer hasStock = 0;
    private String skuPrice;
    /**
     * 销售的属性
     */
    private List<String> attrs;

    private Integer pageNum;//页码


}
