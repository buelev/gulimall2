package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.AttrEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author buelev
 * @email 172319516@qq.com
 * @date 2020-07-01 22:59:55
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params, Long arrtId, String attrType);

    /**
     * 根据属性id查询基础属性
     * @param attrId
     * @return
     */
    AttrEntity findAttrByAttrId(Long attrId);

    /**
     * 修改
     * @param attr
     */
    void updateAttr(AttrEntityVO attr);
}

