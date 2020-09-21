package com.atguigu.gulimall.common.to.es;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * description: SkuEsModel
 * date: 2020-09-16 20:21
 * author: buelev
 * version: 1.0
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;

    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attrs> attrs;

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
