package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.vo.AttrEntityVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Resource
    private AttrGroupDao attrGroupDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(obj -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        if (categoryId != 0) {
            wrapper.eq("catelog_id", categoryId);
        }
        IPage<AttrEntity> page = this.page(
                new Query().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrEntityVO> list = records.stream().map(attrEntity -> {
            AttrEntityVO attrEntityVO = new AttrEntityVO();
            BeanUtils.copyProperties(attrEntity, attrEntityVO);
            //查询分类
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrEntityVO.setCatelogName(categoryEntity.getName());
            }
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (relationEntity != null) {
                AttrGroupEntity groupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                attrEntityVO.setGroupName(groupEntity.getAttrGroupName());
            }
            return attrEntityVO;
        }).collect(Collectors.toList());
        pageUtils.setList(list);
        return pageUtils;
    }

}