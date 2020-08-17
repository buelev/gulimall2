package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 品牌
 *
 * @author buelev
 * @email 172319516@qq.com
 * @date 2020-07-01 22:59:55
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名称不能为空【name】")
    private String name;
    /**
     * 品牌logo地址
     */
    @URL(message = "【logo】必须是一个url地址")
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotBlank(message = "检索首字母不能为空")
    private String firstLetter;
    /**
     * 排序
     */
    @Min(value = 1, message = "排序字段【sort】必须大于1")
    private Integer sort;

}
