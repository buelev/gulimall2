package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.vo.AttrEntityVO;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

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
    public PageUtils queryPage(Map<String, Object> params, Long categoryId, String attrType) {
        String key = (String) params.get("key");
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(obj -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        if (StringUtils.equals("base", attrType)) {
            wrapper.eq("value_type", 0);
        } else {
            wrapper.eq("value_type", 1);
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

    @Override
    public AttrEntity findAttrByAttrId(Long attrId) {
        //查询基础属性
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        AttrEntityVO attrEntityVO = new AttrEntityVO();
        BeanUtils.copyProperties(attrEntity, attrEntityVO);
        //根据catelog查询属性具体的属性
        List<Long> list = new ArrayList<>();
        List<Long> paths = findAllCategory(list, attrEntity.getCatelogId());
        Collections.reverse(paths);
        Long[] categoryPaths = paths.toArray(new Long[]{});
        attrEntityVO.setCatelogPath(categoryPaths);
        //根据属性的id，查询所属的属性分组
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id", attrEntity.getAttrId());
        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(wrapper);
        if (relationEntity == null) {
            return attrEntityVO;
        }
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
        attrEntityVO.setGroupName(attrGroupEntity.getAttrGroupName());
        attrEntityVO.setAttrGroupId(attrGroupEntity.getAttrGroupId());
        return attrEntityVO;
    }

    @Override
    @Transactional
    public void updateAttr(AttrEntityVO attr) {
        //更新基本属性
        this.updateById(attr);
        //根性属性和分类的关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();

        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id", attr.getAttrId());
        Integer count = attrAttrgroupRelationDao.selectCount(wrapper);
        if (count > 0) {
            attrAttrgroupRelationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        } else {
            attrAttrgroupRelationDao.insert(relationEntity);
        }
        //更新属性和分组的关联关系
    }

    /**
     * 根据cate_id查询属性
     *
     * @param list
     * @param catelogId
     * @return
     */
    private List<Long> findAllCategory(List<Long> list, Long catelogId) {
        if (catelogId != 0) {

            list.add(catelogId);
            CategoryEntity entity = categoryDao.selectById(catelogId);
            if (entity != null) {
                findAllCategory(list, entity.getParentCid());
            }
            return list;
        }
        return list;
    }

}