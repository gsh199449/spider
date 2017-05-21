package com.gs.spider.dao;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gs.spider.model.commons.SpiderInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * SpiderInfoDAO
 *
 * @author Gao Shen
 * @version 16/7/18
 */
@Component
public class SpiderInfoDAO extends IDAO<SpiderInfo> {
    private final static Logger LOG = LogManager.getLogger(SpiderInfoDAO.class);
    private final static String INDEX_NAME = "spiderinfo", TYPE_NAME = "spiderinfo";
    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    public SpiderInfoDAO(ESClient esClient) {
        super(esClient, INDEX_NAME, TYPE_NAME);
    }

    public SpiderInfoDAO() {
    }

    @Override
    public String index(SpiderInfo spiderInfo) {
        IndexResponse indexResponse;
        if (getByDomain(spiderInfo.getDomain(), 10, 1).size() > 0) {
            List<SpiderInfo> mayDuplicate = Lists.newLinkedList();
            List<SpiderInfo> temp;
            int i = 1;
            do {
                temp = getByDomain(spiderInfo.getDomain(), 100, i++);
                mayDuplicate.addAll(temp);
            } while (temp.size() > 0);
            if (mayDuplicate.indexOf(spiderInfo) != -1 && (spiderInfo = mayDuplicate.get(mayDuplicate.indexOf(spiderInfo))) != null) {
                LOG.warn("已经含有此模板,不再存储");
                return spiderInfo.getId();
            }
        }
        try {
            indexResponse = client.prepareIndex(INDEX_NAME, TYPE_NAME)
                    .setSource(gson.toJson(spiderInfo))
                    .get();
            LOG.debug("索引爬虫模板成功");
            return indexResponse.getId();
        } catch (Exception e) {
            LOG.error("索引 Webpage 出错," + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected boolean check() {
        return esClient.checkSpiderInfoIndex() && esClient.checkSpiderInfoType();
    }

    private SpiderInfo warpHits2Info(SearchHit hit) {
        SpiderInfo spiderInfo = gson.fromJson(hit.getSourceAsString(), SpiderInfo.class);
        spiderInfo.setId(hit.getId());
        return spiderInfo;
    }

    private SpiderInfo warpHits2Info(String jsonSource, String id) {
        SpiderInfo spiderInfo = gson.fromJson(jsonSource, SpiderInfo.class);
        spiderInfo.setId(id);
        return spiderInfo;
    }

    private List<SpiderInfo> warpHits2List(SearchHits hits) {
        List<SpiderInfo> spiderInfoList = Lists.newLinkedList();
        hits.forEach(searchHitFields -> {
            spiderInfoList.add(warpHits2Info(searchHitFields));
        });
        return spiderInfoList;
    }

    /**
     * 列出库中所有爬虫模板
     *
     * @param size 页面容量
     * @param page 页码
     * @return
     */
    public List<SpiderInfo> listAll(int size, int page) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(size).setFrom(size * (page - 1));
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        return warpHits2List(response.getHits());
    }

    /**
     * 根据domain获取结果
     *
     * @param domain 网站域名
     * @param size   每页数量
     * @param page   页码
     * @return
     */
    public List<SpiderInfo> getByDomain(String domain, int size, int page) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setQuery(QueryBuilders.matchQuery("domain", domain).operator(Operator.AND))
                .setSize(size).setFrom(size * (page - 1));
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        return warpHits2List(response.getHits());
    }

    /**
     * 根据爬虫模板id获取指定爬虫模板
     *
     * @param id 爬虫模板id
     * @return
     */
    public SpiderInfo getById(String id) {
        GetResponse response = client.prepareGet(INDEX_NAME, TYPE_NAME, id).get();
        Preconditions.checkArgument(response.isExists(), "无法找到ID为%s的模板,请检查参数", id);
        return warpHits2Info(response.getSourceAsString(), id);
    }

    /**
     * 根据网站domain删除数据
     *
     * @param domain 网站域名
     * @return 是否全部数据删除成功
     */
    public boolean deleteByDomain(String domain) {
        return deleteByQuery(QueryBuilders.matchQuery("domain", domain), null);
    }

    /**
     * 根据id删除网页模板
     *
     * @param id 网页模板id
     * @return 是否删除
     */
    public boolean deleteById(String id) {
        DeleteResponse response = client.prepareDelete(INDEX_NAME, TYPE_NAME, id).get();
        return response.getResult() == DeleteResponse.Result.DELETED;
    }

    /**
     * 更新爬虫模板
     *
     * @param spiderInfo 爬虫模板实体
     * @return 爬虫模板id
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String update(SpiderInfo spiderInfo) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(spiderInfo.getId()), "待更新爬虫模板id不可为空");
        UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME, TYPE_NAME, spiderInfo.getId());
        updateRequest.doc(gson.toJson(spiderInfo));
        UpdateResponse updateResponse = null;
        try {
            updateResponse = client.update(updateRequest).get();
            return updateResponse.getId();
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new Exception("没有此ID的模板，请删除ID字段的值或者使用正确的id值");
        }
    }
}
