package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.util.UuidUtils;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.IndexCategoryVO;
import net.sf.jsqlparser.statement.create.table.Index;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

//    @Autowired
//    CategoryDao categoryDao;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构

        //2.1）、找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO  1、检查当前删除的菜单，是否被别的地方引用
        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //[2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     *
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    @Override
    public List<CategoryEntity> indexCategory() {
        //查询一级类目数据
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_cid", 0);
        wrapper.orderByDesc("sort");
        return this.baseMapper.selectList(wrapper);
    }

    /**
     * 缓存穿透 雪崩 击穿
     *
     * @return
     */
    @Override
    public Map<String, List<IndexCategoryVO>> getSecondCategoryJson() {
        //首先从缓存中加载数据
        String key = "gulimall.product.index.catalog2.data";
        String catalogJsonString = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(catalogJsonString)) {
            System.out.println("击中缓存中的数据.....");
            //将json数据转为map
            Map<String, List<IndexCategoryVO>> map = JSON.parseObject(catalogJsonString, new TypeReference<Map<String, List<IndexCategoryVO>>>() {
            });
            return map;
        }
        //首先加锁，保证redis分布式锁的原子性
        String uuid = UuidUtils.generateUuid();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("gulimall.product.index.lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            //从数据库中查询加载数据
            Map<String, List<IndexCategoryVO>> catalogFromDB;
            try {
                System.out.println("查询数据库.....");
                catalogFromDB = getCatalogFromDB();
                //将数据保存到缓存中
                redisTemplate.opsForValue().set(key, JSON.toJSONString(catalogFromDB));
            } finally {
                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("gulimall.product.index.lock"), uuid);
            }
            return catalogFromDB;
        } else {
            //锁被抢占，后面的其他线层重试，尝试获取分布式锁
            System.out.println("自旋锁重试获取分布式锁....");
            return getSecondCategoryJson();
        }
    }

    private Map<String, List<IndexCategoryVO>> getCatalogFromDB() {
        //查询所有分类数据
        List<CategoryEntity> entities = this.listWithTree();
        Map<String, List<IndexCategoryVO>> vo = new HashMap<>();
        entities.forEach(entity -> {
            List<IndexCategoryVO> list = entity.getChildren().stream().map(l2 -> {
                List<IndexCategoryVO.Catalog3VO> catalog3VOS = l2.getChildren().stream().map(l3 -> {
                    IndexCategoryVO.Catalog3VO l3Category = IndexCategoryVO.Catalog3VO.builder()
                            .catalog2Id(l2.getCatId().toString())
                            .id(l3.getCatId().toString())
                            .name(l3.getName())
                            .build();
                    return l3Category;
                }).collect(Collectors.toList());
                IndexCategoryVO indexCategoryVO = IndexCategoryVO.builder()
                        .catalog1Id(entity.getCatId().toString())
                        .id(l2.getCatId().toString())
                        .name(l2.getName())
                        .catalog3List(catalog3VOS)
                        .build();
                return indexCategoryVO;
            }).collect(Collectors.toList());
            vo.put(entity.getCatId().toString(), list);
        });
        return vo;
    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;

    }


    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2、菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }


}