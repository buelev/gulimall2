package com.atguigu.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gulimall.common.constant.ESConstant;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.search.config.ElasticSearchConfig;
import com.atguigu.gulimall.search.service.MallSearchService;
import com.atguigu.gulimall.search.vo.GuliMallSearchResponse;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * description: MallSearchServiceImpl
 * date: 2020-10-23 15:50
 * author: buelev
 * version: 1.0
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Resource
    private RestHighLevelClient client;

    @Override
    public GuliMallSearchResponse search(SearchParam searchParam) {
        SearchRequest request = bulidSearchRequest(searchParam);
        try {
            SearchResponse result = client.search(request, ElasticSearchConfig.COMMON_OPTIONS);
            GuliMallSearchResponse response = buildSearchReponse(result, searchParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构建搜索的结果
     *
     * @param result
     * @return
     */
    private GuliMallSearchResponse buildSearchReponse(SearchResponse result, SearchParam searchParam) {
        GuliMallSearchResponse response = GuliMallSearchResponse.builder().build();
        //构建商品的数据
        SearchHit[] hits = result.getHits().getHits();
        List<SkuEsModel> productList = new ArrayList<>();
        for (SearchHit hit : hits) {
            String productJson = hit.getSourceAsString();
            SkuEsModel skuEsModel = JSON.parseObject(productJson, SkuEsModel.class);
            productList.add(skuEsModel);
        }
        response.setProducts(productList);
        //分页信息 11/2 5+1
        long total = result.getHits().getTotalHits().value;//总记录数
        int totalPage = total / ESConstant.PRODUCT_PAGESIZE == 0 ? (int) total / ESConstant.PRODUCT_PAGESIZE : (int) total / ESConstant.PRODUCT_PAGESIZE + 1;
        response.setTotal(total);
        response.setPageSize(totalPage);
        //商品的分类的信息
        ParsedLongTerms catalogAggs = result.getAggregations().get("catalog_aggs");
        List<GuliMallSearchResponse.CatalogVo> catalogList = new ArrayList<>();
        List<? extends Terms.Bucket> catalogIdBuckets = catalogAggs.getBuckets();
        for (Terms.Bucket catalogIdBucket : catalogIdBuckets) {
            Long catalogId = catalogIdBucket.getKeyAsNumber().longValue();
            ParsedStringTerms catalogNameAgg = catalogIdBucket.getAggregations().get("catalog_name_agg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            GuliMallSearchResponse.CatalogVo catalogVo = GuliMallSearchResponse.CatalogVo.builder()
                    .catalogId(catalogId)
                    .catalogName(catalogName)
                    .build();
            catalogList.add(catalogVo);
        }
        response.setCatalogs(catalogList);
        //所有的品牌
        ParsedLongTerms brandAggs = result.getAggregations().get("brand_aggs");
        List<? extends Terms.Bucket> brandAggsBuckets = brandAggs.getBuckets();
        List<GuliMallSearchResponse.BrandVO> brandlist = new ArrayList<>();
        for (Terms.Bucket brandAggsBucket : brandAggsBuckets) {
            Long brandId = brandAggsBucket.getKeyAsNumber().longValue();
            ParsedStringTerms brandNameAggs = brandAggsBucket.getAggregations().get("brand_name_aggs");
            String brandName = brandNameAggs.getBuckets().get(0).getKeyAsString();
            ParsedStringTerms brandImgAggs = brandAggsBucket.getAggregations().get("brand_img_aggs");
            String brandImg = brandImgAggs.getBuckets().get(0).getKeyAsString();
            GuliMallSearchResponse.BrandVO brandVO = GuliMallSearchResponse.BrandVO.builder()
                    .brandId(brandId)
                    .brandName(brandName)
                    .brandImg(brandImg)
                    .build();
            brandlist.add(brandVO);
        }
        response.setBrands(brandlist);

        //商品属性
        ParsedNested attrNested = result.getAggregations().get("attrNested");
        ParsedLongTerms attrIdAggs = attrNested.getAggregations().get("attr_id_aggs");
        List<? extends Terms.Bucket> buckets = attrIdAggs.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            long attrId = bucket.getKeyAsNumber().longValue();
            ParsedStringTerms attrValueAggs = bucket.getAggregations().get("attr_value_aggs");
        }
        return null;
    }

    /**
     * 构建搜索的DSL语句
     *
     * @param searchParam
     * @return
     */
    private SearchRequest bulidSearchRequest(SearchParam searchParam) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //关键字查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (Objects.nonNull(searchParam.getKeyword())) {
            builder.query(queryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword())));
            //高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field(searchParam.getKeyword());
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            builder.highlighter(highlightBuilder);
        } else {
            builder.query(queryBuilder.must(QueryBuilders.matchAllQuery()));
        }
        if (Objects.nonNull(searchParam.getCatalogId())) {
            queryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalogId()));
        }
        if (Objects.nonNull(searchParam.getBrandId()) && searchParam.getBrandId().size() > 0) {
            queryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }
        //属性查询
        if (Objects.nonNull(searchParam.getAttrs()) && searchParam.getAttrs().size() > 0) {
            //attrs=1_5寸:8寸&attrs=2_16G:32G
            List<String> attrs = searchParam.getAttrs();
            for (String attr : attrs) {
                //分割数据
                String[] attrArr = attr.split("_");
                String[] attrValueArr = attrArr[1].split(":");
                BoolQueryBuilder attrBuilder = QueryBuilders.boolQuery();
                BoolQueryBuilder attrQueryBuilder = attrBuilder.must(QueryBuilders.termQuery("attrs.attrId", attrArr[0])).must(QueryBuilders.termsQuery("attrs.attrValue", attrValueArr));
                queryBuilder.filter(QueryBuilders.nestedQuery("attrs", attrQueryBuilder, ScoreMode.None));
            }
        }
        //是否有库存
        if (Objects.nonNull(searchParam.getHasStock())) {
            queryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1));
        }
        //排序
        if (Objects.nonNull(searchParam.getSort())) {
            String[] sort = searchParam.getSort().split("_");
            builder.sort(sort[0], StringUtils.equalsIgnoreCase("desc", sort[1]) ? SortOrder.DESC : SortOrder.ASC);
        }
        //分页
        builder.from((searchParam.getPageNum() - 1) * ESConstant.PRODUCT_PAGESIZE);
        builder.size(ESConstant.PRODUCT_PAGESIZE);

        //聚合分析
        //品牌聚合
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_aggs");
        brandAgg.field("brandId").size(10);
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_aggs").field("brandName").size(10));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_aggs").field("brandImg").size(10));
        builder.aggregation(brandAgg);
        //分类聚合
        TermsAggregationBuilder catalogAggs = AggregationBuilders.terms("catalog_aggs");
        catalogAggs.field("catalogId").size(10);
        catalogAggs.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(10));
        builder.aggregation(catalogAggs);
        //属性聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrNested", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_aggs").field("attrs.attrId").size(10);
        TermsAggregationBuilder attrNameAggs = attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_aggs").field("attrs.attrName").size(1));
        attrNameAggs.subAggregation(AggregationBuilders.terms("attr_value_aggs").field("attrs.attrValue").size(10));
        attrAgg.subAggregation(attrIdAgg);
        builder.aggregation(attrAgg);
        SearchRequest request = new SearchRequest(new String[]{ESConstant.PRODUCT_INDEX}, builder);
        System.out.println(request);
        return request;
    }
}
