package com.atguigu.gulimall.product.vo;

import lombok.*;

import java.util.List;

/**
 * description: IndexCategoryVO
 * date: 2020-09-22 11:23
 * author: buelev
 * version: 1.0
 * 首页分类的数据
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IndexCategoryVO {
    private String catalog1Id;
    private String id;
    private String name;
    private List<Catalog3VO> catalog3List;

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Catalog3VO {
        private String catalog2Id;
        private String id;
        private String name;
    }
}
