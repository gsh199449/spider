package com.gs.spider.dao;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.gson.*;
import com.gs.spider.model.commons.SpiderInfo;
import com.gs.spider.model.commons.Webpage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * CommonWebpagePipeline
 *
 * @author Gao Shen
 * @version 16/4/12
 */
@Component
public class CommonWebpagePipeline extends IDAO<Webpage> implements DuplicateRemover, Pipeline {
    private final static String INDEX_NAME = "commons", TYPE_NAME = "webpage";
    private static final String DYNAMIC_FIELD = "dynamic_fields";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getTime()))
            .setDateFormat(DateFormat.LONG).create();
    private static int COUNT = 0;
    private Logger LOG = LogManager.getLogger(CommonWebpagePipeline.class);
    private Map<String, Set<String>> urls = Maps.newConcurrentMap();

    @Autowired
    public CommonWebpagePipeline(ESClient esClient) {
        super(esClient, INDEX_NAME, TYPE_NAME);
    }

    /**
     * 将webmagic的resultItems转换成webpage对象
     *
     * @param resultItems
     * @return
     */
    public static Webpage convertResultItems2Webpage(ResultItems resultItems) {
        Webpage webpage = new Webpage();
        webpage.setContent(resultItems.get("content"));
        webpage.setTitle(resultItems.get("title"));
        webpage.setUrl(resultItems.get("url"));
        webpage.setId(Hashing.md5().hashString(webpage.getUrl(), Charset.forName("utf-8")).toString());
        webpage.setDomain(resultItems.get("domain"));
        webpage.setSpiderInfoId(resultItems.get("spiderInfoId"));
        webpage.setGathertime(resultItems.get("gatherTime"));
        webpage.setSpiderUUID(resultItems.get("spiderUUID"));
        webpage.setKeywords(resultItems.get("keywords"));
        webpage.setSummary(resultItems.get("summary"));
        webpage.setNamedEntity(resultItems.get("namedEntity"));
        webpage.setPublishTime(resultItems.get("publishTime"));
        webpage.setCategory(resultItems.get("category"));
        webpage.setRawHTML(resultItems.get("rawHTML"));
        webpage.setDynamicFields(resultItems.get(DYNAMIC_FIELD));
        webpage.setStaticFields(resultItems.get("staticField"));
        webpage.setAttachmentList(resultItems.get("attachmentList"));
        webpage.setImageList(resultItems.get("imageList"));
        webpage.setProcessTime(resultItems.get("processTime"));
        return webpage;
    }

    @Override
    public String index(Webpage webpage) {
        return null;
    }

    @Override
    protected boolean check() {
        return esClient.checkCommonsIndex() && esClient.checkWebpageType();
    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        Set<String> tempLists = urls.computeIfAbsent(task.getUUID(), k -> Sets.newConcurrentHashSet());
        //初始化已采集网站列表缓存
        if (tempLists.add(request.getUrl())) {//先检查当前生命周期是否抓取过,如果当前生命周期未抓取,则进一步检查ES
            GetResponse response = client.prepareGet(INDEX_NAME, TYPE_NAME,
                    Hashing.md5().hashString(request.getUrl(), Charset.forName("utf-8")).toString()
            ).get();
            return response.isExists();
        } else {//如果当前生命周期已抓取,直接置为重复
            return true;
        }

    }

    @Override
    public void resetDuplicateCheck(Task task) {

    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return COUNT++;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        SpiderInfo spiderInfo = resultItems.get("spiderInfo");
        Webpage webpage = convertResultItems2Webpage(resultItems);
        try {
            client.prepareIndex(INDEX_NAME, TYPE_NAME)
                    .setId(Hashing.md5().hashString(webpage.getUrl(), Charset.forName("utf-8")).toString())
                    .setSource(gson.toJson(webpage))
                    .get();
        } catch (Exception e) {
            LOG.error("索引 Webpage 出错," + e.getLocalizedMessage());
        }
    }

    /**
     * 清除已停止任务的抓取url列表
     *
     * @param taskId 任务id
     */
    public void deleteUrls(String taskId) {
        urls.remove(taskId);
        LOG.info("任务{}已结束,抓取列表缓存已清除", taskId);
    }
}
