package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * description: AttrGroupEntityVo
 * date: 2020-08-19 20:28
 * author: buelev
 * version: 1.0
 * 业务层之间的数据传递，也叫做View object
 */
@Data
public class AttrGroupEntityVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 属性分组的分类的路径
     */
    private Long[] categoryPath;
}
