package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
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

}
