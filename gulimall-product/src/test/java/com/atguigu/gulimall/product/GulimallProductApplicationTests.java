package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private BrandService brandService;

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

}
