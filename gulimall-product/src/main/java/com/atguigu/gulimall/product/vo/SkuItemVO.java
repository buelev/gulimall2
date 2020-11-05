package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import lombok.*;

import java.util.List;

/**
 * description: SkuItemVO
 * date: 2020-11-04 16:48
 * author: buelev
 * version: 1.0
 */
@Data
@Builder
@ToString
public class SkuItemVO {
    //sku的基本信息
    private SkuInfoEntity skuInfoEntity;
    //sku的图片信息
    private List<SkuImagesEntity> skuImagesEntity;
    //spu的销售属性的组合
    private List<SkuItemSaleAttrVo> skuItemSaleAttrVo;
    //spu的介绍
    private SpuInfoDescEntity spuInfoDescEntity;
    //spu的规格参数
    private List<SpuSaleAttrGroupVo> spuSaleAttrGroupVo;

    @Data
    @Builder
    @ToString
    public static class SkuItemSaleAttrVo {
        private Long spuId;
        private String spuName;
        private List<String> spuValue;
    }

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpuSaleAttrGroupVo {
        private String groupName;
        private List<SpuSaleAttr> spuSaleAttr;
    }

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpuSaleAttr {
        private String attrName;
        private String attrValue;
    }
}



