package com.atguigu.gulimall.search.vo;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * description: SearchResponse
 * date: 2020-10-28 14:36
 * author: buelev
 * version: 1.0
 */
@Data
@Builder
@ToString
public class GuliMallSearchResponse {
    //查询的所有的商品的信息
    private List<SkuEsModel> products;
    //分页信息
    private Integer pageSize;//总页码
    private Long total;//总记录数
    private List<BrandVO> brands;//所有的涉及到的品牌
    private List<CatalogVo> catalogs;//所有涉及到的分类
    private List<Attrs> attrs;//所有涉及到的属性

    @Data
    @Builder
    public static class BrandVO {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    @Builder
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    @Builder
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }
}
