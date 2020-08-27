package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.service.AttrAttrgroupRelationService;

import javax.annotation.Resource;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Resource
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> findAttrGroupAndAttrRelation(Long attrGroupId) {
        //根据属性分组id查询所有对应的属性的id
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_group_id", attrGroupId);
        List<AttrAttrgroupRelationEntity> list = baseMapper.selectList(wrapper);
        //根据id查询具体的属性信息
        List<AttrEntity> attrEntityList = new ArrayList<>();
        list.forEach(attr -> {
            AttrEntity attrEntity = attrDao.selectById(attr.getAttrId());
            attrEntityList.add(attrEntity);
        });
        return attrEntityList;
    }

}