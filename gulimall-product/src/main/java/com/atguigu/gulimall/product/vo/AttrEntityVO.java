package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.AttrEntity;
import lombok.Data;

/**
 * description: AttrEntityVO
 * date: 2020-08-27 00:53
 * author: buelev
 * version: 1.0
 */
@Data
public class AttrEntityVO extends AttrEntity {
    private String groupName;
    private String catelogName;
    private Long[] catelogPath;
    private Long attrGroupId;
}
