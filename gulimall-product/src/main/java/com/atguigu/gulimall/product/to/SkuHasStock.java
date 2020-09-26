package com.atguigu.gulimall.product.to;

import lombok.*;

/**
 * description: SkuHasStock
 * date: 2020-09-21 16:54
 * author: buelev
 * version: 1.0
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SkuHasStock {
    private Long skuId;
    private Boolean hasStock;
}
