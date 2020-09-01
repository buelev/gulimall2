package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrGroupEntityVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Resource
    private CategoryService categoryService;
    @Resource
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Integer catId) {
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(obj -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);

            });
        }
        IPage<AttrGroupEntity> page = null;
        if (catId == 0) {
            //查询所有的属性
            page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper);
//            return new PageUtils(page);
        } else {
            queryWrapper.eq("catelog_id", catId);
//            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("cat_id", catId);
//            if (StringUtils.isNotBlank(key)) {
//                wrapper.and(obj -> {
//                    obj.eq("attr_group_id", key).or().like("attr_group_name", key);
//                });
//            }
            page = this.page(new Query<AttrGroupEntity>().getPage(params), queryWrapper);
        }
        return new PageUtils(page);
    }

    @Override
    public AttrGroupEntityVo findAttrGroupCategoryPath(Long attrGroupId, AttrGroupEntity attrGroup) {
        //属性分类
        Long catelogId = attrGroup.getCatelogId();
        //select * from pms_category where parent_id = ?
        List<Long> paths = new ArrayList<>();
        List<Long> list = findCategoryPath(catelogId, paths);
        Collections.reverse(list);
        Long[] pathArray = list.toArray(new Long[]{});
        AttrGroupEntityVo entityVo = new AttrGroupEntityVo();
        BeanUtils.copyProperties(attrGroup, entityVo);
        entityVo.setCategoryPath(pathArray);
        return entityVo;
    }

    @Override
    public PageUtils findAllAttr(Map<String, Object> map) {
        String key = (String) map.get("key");
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(obj->{
                obj.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = attrDao.selectPage(new Query<AttrEntity>().getPage(map), wrapper);
        return new PageUtils(page);
    }

    protected List<Long> findCategoryPath(Long catelogId, List<Long> paths) {
        CategoryEntity entity = categoryService.getById(catelogId);
        if (entity == null) {
            return paths;
        }
        paths.add(entity.getCatId());
        if (entity.getParentCid() != 0) {
            findCategoryPath(entity.getParentCid(), paths);
        }
        return paths;
    }
}