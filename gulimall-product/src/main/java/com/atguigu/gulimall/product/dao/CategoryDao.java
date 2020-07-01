package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author buelev
 * @email 172319516@qq.com
 * @date 2020-07-01 22:59:55
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
