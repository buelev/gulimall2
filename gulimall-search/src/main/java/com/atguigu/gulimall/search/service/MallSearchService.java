package com.atguigu.gulimall.search.service;

import com.atguigu.gulimall.search.vo.GuliMallSearchResponse;
import com.atguigu.gulimall.search.vo.SearchParam;

/**
 * description: MallSearchService
 * date: 2020-10-23 15:50
 * author: buelev
 * version: 1.0
 */
public interface MallSearchService {
    /**
     * 从elasticsearch中搜索数据
     * @param searchParam
     * @return
     */
    GuliMallSearchResponse search(SearchParam searchParam);
}
