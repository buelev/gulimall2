package com.atguigu.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.search.config.ElasticSearchConfig;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;
import org.json.JSONString;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@Slf4j
public class GulimallSearchApplicationTests {

    @Test
    public void testMD2() {
        String value = "0x31";
        for (int i = 0; i < 100000000; i++) {
        }
        System.out.println(value);
        log.info(value);
    }

    @Test
    public void testC() {
        System.out.println("da59e52887eb3782c71135fdd589f2d2".toUpperCase());
    }

    @Data
    @Builder
    static class User {
        private String name;
        private String sex;
        private Integer age;
    }

    @Resource
    private RestHighLevelClient client;

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

    @Test
    public void index() throws IOException {
        IndexRequest request = new IndexRequest("user");
        request.id("2");
        User usr = User.builder()
                .name("宋艺馨")
                .sex("女")
                .age(20)
                .build();
        String user = JSON.toJSONString(usr);
        request.source(user, XContentType.JSON);
        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println("保存数据成功");
                DocWriteResponse.Result result = indexResponse.getResult();
                System.out.println(result);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("保存数据失败");
            }
        };
        client.indexAsync(request, RequestOptions.DEFAULT, listener);
    }

    @Test
    public void searchIndex() throws IOException {
        SearchRequest searchRequest = new SearchRequest("newbank");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.aggregation(AggregationBuilders.terms("ageAgg").field("age").size(10).subAggregation(AggregationBuilders.avg("balanceAgg").field("balance"))).size(0);
        searchRequest.source(sourceBuilder);
        //性能参数，优先查询本地切片中的数据
        searchRequest.preference("_local");
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(search);
        Aggregations aggregations = search.getAggregations();
        List<Aggregation> list = aggregations.asList();
        for (Aggregation aggregation : list) {
            System.out.println(aggregation);
        }
    }
}
