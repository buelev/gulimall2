package com.atguigu.gulimall.ware.to;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * description: SkuHasStock
 * date: 2020-09-21 16:54
 * author: buelev
 * version: 1.0
 */
@Data
@Builder
@ToString
public class SkuHasStock {
    private Long skuId;
    private Boolean hasStock;
}
