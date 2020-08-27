package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.vo.AttrGroupEntityVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author buelev
 * @email 172319516@qq.com
 * @date 2020-07-01 22:59:55
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询当前菜单下的属性
     *
     * @param params
     * @param catId
     * @return
     */
    PageUtils queryPage(Map<String, Object> params, Integer catId);

    /**
     * 查询属性分组中分类的路径
     *
     * @param attrGroupId
     * @param attrGroup
     * @return
     */
    AttrGroupEntityVo findAttrGroupCategoryPath(Long attrGroupId, AttrGroupEntity attrGroup);

    /**
     * 查询所有的属性
     * @param map
     * @return
     */
    PageUtils findAllAttr(Map<String, Object> map);
}

