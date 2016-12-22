package com.gs.spider.dao;

import com.google.common.base.Preconditions;
import com.gs.spider.model.async.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * IDAO
 * Elasticsearch数据接口
 *
 * @author Gao Shen
 * @version 16/1/25
 */
public abstract class IDAO<T> {
    private static final int SCROLL_TIMEOUT = 5;
    protected Client client;
    protected Queue<T> queue = new ConcurrentLinkedDeque<>();
    protected ESClient esClient;
    private Logger LOG = LogManager.getLogger(IDAO.class);
    private String INDEX_NAME, TYPE_NAME;

    public IDAO(ESClient esClient, String index_name, String type_name) {
        this.esClient = esClient;
        this.INDEX_NAME = index_name;
        this.TYPE_NAME = type_name;
        initClient(esClient);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                while (queue.size() > 0) {
                    index(queue.remove());
                }
            }
        }, 10000, 30000);
    }


    public IDAO() {
    }

    /**
     * 初始化ES客户端
     *
     * @param esClient
     * @return
     */
    private boolean initClient(ESClient esClient) {
        if (client != null) {
            LOG.info("已经初始化过了");
        }
        this.client = esClient.getClient();
        LOG.debug("检查ES index,type 是否存在");
        return check();
    }

    /**
     * 索引数据
     *
     * @param t
     */
    public abstract String index(T t);

    /**
     * 检查index和type是否存在
     *
     * @return
     */
    protected abstract boolean check();

    /**
     * 根据query删除数据
     *
     * @param queryBuilder query
     * @param task         任务实体
     * @return 是否全部数据删除成功
     */
    protected boolean deleteByQuery(QueryBuilder queryBuilder, Task task) {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setQuery(queryBuilder)
                .setSize(100)
                .setScroll(TimeValue.timeValueMinutes(SCROLL_TIMEOUT));
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        while (true) {
            for (SearchHit hit : response.getHits()) {
                bulkRequestBuilder.add(new DeleteRequest(INDEX_NAME, TYPE_NAME, hit.id()));
                if (task != null) {
                    task.increaseCount();
                }
            }
            response = client.prepareSearchScroll(response.getScrollId())
                    .setScroll(TimeValue.timeValueMinutes(SCROLL_TIMEOUT))
                    .execute().actionGet();
            if (response.getHits().getHits().length == 0) {
                if (task != null) {
                    task.setDescription("按query%s删除数据ID添加完毕,已经添加%s条,准备执行删除", queryBuilder.toString(), bulkRequestBuilder.numberOfActions());
                }
                LOG.debug("按query{}删除数据ID添加完毕,准备执行删除", queryBuilder.toString());
                break;
            } else {
                if (task != null) {
                    task.setDescription("按query%s删除数据已经添加%s条", queryBuilder.toString(), bulkRequestBuilder.numberOfActions());
                }
                LOG.debug("按query{}删除数据已经添加{}条", queryBuilder.toString(), bulkRequestBuilder.numberOfActions());
            }
        }
        if (bulkRequestBuilder.numberOfActions() <= 0) {
            if (task != null) {
                task.setDescription("按query%s删除数据时未找到数据,请检查参数", queryBuilder.toString());
            }
            LOG.debug("按query{}删除数据时未找到数据,请检查参数", queryBuilder.toString());
            return false;
        }
        BulkResponse bulkResponse = bulkRequestBuilder.get();
        if (bulkResponse.hasFailures()) {
            if (task != null) {
                task.setDescription("按query%s删除数据部分失败,%s", queryBuilder.toString(), bulkResponse.buildFailureMessage());
            }
            LOG.error("按query{}删除数据部分失败,{}", queryBuilder.toString(), bulkResponse.buildFailureMessage());
        } else {
            if (task != null) {
                task.setDescription("按query%s删除数据成功,耗时:%s毫秒", queryBuilder.toString(), bulkResponse.getTookInMillis());
            }
            LOG.info("按query{}删除数据成功,耗时:{}毫秒", queryBuilder.toString(), bulkResponse.getTookInMillis());
        }
        return bulkResponse.hasFailures();
    }

    /**
     * 获取库中数据总数
     *
     * @return
     */
    public Long getTotal() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setQuery(QueryBuilders.matchAllQuery());
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        return response.getHits().getTotalHits();
    }

    /**
     * 获取库中符合条件是数据数量
     *
     * @param queryBuilder 匹配条件
     * @return
     */
    protected Long getCountBy(QueryBuilder queryBuilder) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setQuery(queryBuilder);
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        return response.getHits().getTotalHits();
    }

    /**
     * 导出数据
     *
     * @param queryBuilder    数据查询
     * @param labelsSupplier  提取labels,每篇文章返回一个label的List
     * @param contentSupplier 提取正文
     * @return 经过分词的输出流
     */
    protected void exportData(QueryBuilder queryBuilder, Function<SearchResponse, List<List<String>>> labelsSupplier, Function<SearchResponse, List<String>> contentSupplier, OutputStream outputStream) {
        final int size = 50;
        String scrollId = null;
        for (int page = 1; ; page++) {
            LOG.debug("正在输出{}第{}页", queryBuilder, page);
            SearchResponse response;
            if (StringUtils.isBlank(scrollId)) {
                response = client.prepareSearch(INDEX_NAME)
                        .setTypes(TYPE_NAME)
                        .setQuery(queryBuilder)
                        .setSize(size)
                        .setScroll(TimeValue.timeValueMinutes(SCROLL_TIMEOUT))
                        .execute().actionGet();
                scrollId = response.getScrollId();
            } else {
                response = client.prepareSearchScroll(scrollId)
                        .setScroll(TimeValue.timeValueMinutes(SCROLL_TIMEOUT)).execute().actionGet();
            }
            final List<List<String>> labels = labelsSupplier.apply(response);
            final List<String> contentList = contentSupplier.apply(response);
            Preconditions.checkNotNull(labels);
            Preconditions.checkNotNull(contentList);
            if (contentList.size() <= 0) break;
            List<String> combine;
            if (labels.size() > 0) {
                combine = labels.stream().map(labelList ->
                        labelList.parallelStream().collect(Collectors.joining("/")
                        )).collect(Collectors.toList());
                for (int i = 0; i < labels.size(); i++) {
                    String content = contentList.get(i);
                    combine.set(i, combine.get(i) + " " + content);
                }
            } else {
                combine = contentList;
            }
            try {
                IOUtils.write(combine.stream().collect(Collectors.joining("\n")) + "\n", outputStream, "utf-8");
                outputStream.flush();
            } catch (IOException e) {
                LOG.error("输出错误,{}", e.getLocalizedMessage());
            }
        }
    }
}
