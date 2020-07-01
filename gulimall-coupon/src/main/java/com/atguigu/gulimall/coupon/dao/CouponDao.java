package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author buelev
 * @email 172319516@qq.com
 * @date 2020-07-02 01:33:08
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
