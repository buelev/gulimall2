package com.atguigu.mall.thirdparty;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private OSS ossClient;

    @Value("${spring.cloud.alicloud.oss.bucketname}")
    private String buckeName;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessKey;

    @Value("${spring.cloud.alicloud.access-key}")
    private String scretKey;

    @Test
    public void ossTest() throws FileNotFoundException {
        ossClient.putObject(buckeName,accessKey,new FileInputStream("/Users/buelev/Downloads/IMG_1607.JPG"));
        System.out.println("文件上传成功");
    }

}
