package com.atguigu.gulimall.product;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.product.dao.SkuInfoDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.vo.SkuItemVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallProductApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private BrandService brandService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void save() {
        BrandEntity entity = new BrandEntity();
        entity.setDescript("你猜啊");
        brandService.save(entity);
    }

    @Test
    public void delete() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(13l);
        brandService.removeById(13l);
    }

    @Test
    public void update() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setDescript("mybatis-plus");
        brandEntity.setBrandId(14l);
        brandService.updateById(brandEntity);
    }

    @Test
    public void redisTest() {
        redisTemplate.opsForValue().set(12, JSON.toJSONString(12));
    }

    @Resource
    private SkuInfoDao skuInfoDao;

    @Test
    public void testSkuInfo() {
        List<SkuItemVO.SpuSaleAttrGroupVo> vos = skuInfoDao.querySpuSaleAttrGroupVO(13l, 225l);
        System.out.println(JSON.toJSONString(vos));
    }

    @Test
    public void testSkuSaleAttrValue() {
        List<SkuItemVO.SkuItemSaleAttrVo> attrVos = skuInfoDao.selectSpuItemSaleAttr(13l);
        System.out.println(JSON.toJSONString(attrVos));
    }
}
