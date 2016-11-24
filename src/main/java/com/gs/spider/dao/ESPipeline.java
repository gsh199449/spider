package com.gs.spider.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * NewsPipeline
 *
 * @author Gao Shen
 * @version 16/3/1
 */
public abstract class ESPipeline extends IDAO implements Pipeline {
    private final String INDEX_NAME, TYPE_NAME;
    private Logger LOG = LogManager.getLogger(ESPipeline.class);

    public ESPipeline(ESClient esClient, String indexName, String typeName) {
        super(esClient, indexName, typeName);
        this.INDEX_NAME = indexName;
        this.TYPE_NAME = typeName;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        Iterator i$ = resultItems.getAll().entrySet().iterator();
        try {
            XContentBuilder xContentBuilder = jsonBuilder().startObject();
            while (i$.hasNext()) {
                Map.Entry entry = (Map.Entry) i$.next();
                xContentBuilder.field((String) entry.getKey(), entry.getValue());
            }
            String json = xContentBuilder.endObject().string();
            IndexResponse response = null;
            if (StringUtils.isNotBlank(resultItems.get("id"))) {
                response = client
                        .prepareIndex(INDEX_NAME, TYPE_NAME, resultItems.get("id"))
                        .setSource(json).get();
            } else {
                response = client
                        .prepareIndex(INDEX_NAME, TYPE_NAME)
                        .setSource(json).get();
            }
            if (response.getResult() != IndexResponse.Result.CREATED)
                LOG.error("索引失败,可能重复创建,resultItem:" + resultItems);
        } catch (IOException e) {
            LOG.error("索引出错," + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
