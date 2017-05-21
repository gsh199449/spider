package com.gs.spider.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by gaoshen on 16/1/14.
 */
@Component
@Scope("singleton")
public class StaticValue {
    public String esHost;
    public int esPort;
    private Logger LOG = LogManager.getLogger(StaticValue.class);
    private String commonsIndex;
    private int commonSpiderTaskManagerPort;
    private long maxHttpDownloadLength;
    private boolean commonsSpiderDebug;
    private String esClusterName;
    /**
     * 删除任务延时,单位为小时
     */
    private int taskDeleteDelay;
    /**
     * 删除任务时间间隔,单位为小时
     */
    private int taskDeletePeriod;
    /**
     * 普通网页下载器队列最大长度限制
     */
    private int limitOfCommonWebpageDownloadQueue;
    /**
     * 是否需要Redis
     */
    private boolean needRedis;
    private boolean needEs;
    private int redisPort;
    private String redisHost;
    private String webpageRedisPublishChannelName;
    /**
     * 抓取页面比例,如果抓取页面超过最大抓取数量ratio倍的时候仍未达到最大抓取数量爬虫也退出
     */
    private int commonsWebpageCrawlRatio;
    private String ajaxDownloader;

    public StaticValue() {
        LOG.debug("正在初始化StaticValue");
        try {
            String json = FileUtils.readFileToString(new File(this.getClass().getClassLoader()
                    .getResource("staticvalue.json").getFile()));
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            this.esHost = jsonObject.get("esHost").getAsString();
            this.esPort = jsonObject.get("esPort").getAsInt();
            this.esClusterName = jsonObject.get("esClusterName").getAsString();
            this.commonsIndex = jsonObject.get("commonsIndex").getAsString();
            this.maxHttpDownloadLength = jsonObject.get("maxHttpDownloadLength").getAsLong();
            this.commonsSpiderDebug = jsonObject.get("commonsSpiderDebug").getAsBoolean();
            this.taskDeleteDelay = jsonObject.get("taskDeleteDelay").getAsInt();
            this.taskDeletePeriod = jsonObject.get("taskDeletePeriod").getAsInt();
            this.limitOfCommonWebpageDownloadQueue = jsonObject.get("limitOfCommonWebpageDownloadQueue").getAsInt();
            this.redisPort = jsonObject.get("redisPort").getAsInt();
            this.redisHost = jsonObject.get("redisHost").getAsString();
            this.needRedis = jsonObject.get("needRedis").getAsBoolean();
            this.needEs = jsonObject.get("needEs").getAsBoolean();
            this.webpageRedisPublishChannelName = jsonObject.get("webpageRedisPublishChannelName").getAsString();
            this.commonsWebpageCrawlRatio = jsonObject.get("commonsWebpageCrawlRatio").getAsInt();
            this.ajaxDownloader = jsonObject.get("ajaxDownloader").getAsString();
            LOG.debug("StaticValue初始化成功," + this);
        } catch (IOException e) {
            LOG.fatal("初始化StaticValue失败," + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public String getEsHost() {
        return esHost;
    }

    public void setEsHost(String esHost) {
        this.esHost = esHost;
    }

    public int getEsPort() {
		return esPort;
	}

	public void setEsPort(int esPort) {
		this.esPort = esPort;
	}

	public String getCommonsIndex() {
        return commonsIndex;
    }

    public StaticValue setCommonsIndex(String commonsIndex) {
        this.commonsIndex = commonsIndex;
        return this;
    }

    public int getCommonSpiderTaskManagerPort() {
        return commonSpiderTaskManagerPort;
    }

    public StaticValue setCommonSpiderTaskManagerPort(int commonSpiderTaskManagerPort) {
        this.commonSpiderTaskManagerPort = commonSpiderTaskManagerPort;
        return this;
    }

    public long getMaxHttpDownloadLength() {
        return maxHttpDownloadLength;
    }

    public StaticValue setMaxHttpDownloadLength(long maxHttpDownloadLength) {
        this.maxHttpDownloadLength = maxHttpDownloadLength;
        return this;
    }

    public boolean isCommonsSpiderDebug() {
        return commonsSpiderDebug;
    }

    public StaticValue setCommonsSpiderDebug(boolean commonsSpiderDebug) {
        this.commonsSpiderDebug = commonsSpiderDebug;
        return this;
    }

    public String getEsClusterName() {
        return esClusterName;
    }

    public StaticValue setEsClusterName(String esClusterName) {
        this.esClusterName = esClusterName;
        return this;
    }

    public int getTaskDeleteDelay() {
        return taskDeleteDelay;
    }

    public StaticValue setTaskDeleteDelay(int taskDeleteDelay) {
        this.taskDeleteDelay = taskDeleteDelay;
        return this;
    }

    public int getTaskDeletePeriod() {
        return taskDeletePeriod;
    }

    public StaticValue setTaskDeletePeriod(int taskDeletePeriod) {
        this.taskDeletePeriod = taskDeletePeriod;
        return this;
    }

    public int getLimitOfCommonWebpageDownloadQueue() {
        return limitOfCommonWebpageDownloadQueue;
    }

    public StaticValue setLimitOfCommonWebpageDownloadQueue(int limitOfCommonWebpageDownloadQueue) {
        this.limitOfCommonWebpageDownloadQueue = limitOfCommonWebpageDownloadQueue;
        return this;
    }

    public boolean isNeedRedis() {
        return needRedis;
    }

    public StaticValue setNeedRedis(boolean needRedis) {
        this.needRedis = needRedis;
        return this;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public StaticValue setRedisPort(int redisPort) {
        this.redisPort = redisPort;
        return this;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public StaticValue setRedisHost(String redisHost) {
        this.redisHost = redisHost;
        return this;
    }

    public String getWebpageRedisPublishChannelName() {
        return webpageRedisPublishChannelName;
    }

    public StaticValue setWebpageRedisPublishChannelName(String webpageRedisPublishChannelName) {
        this.webpageRedisPublishChannelName = webpageRedisPublishChannelName;
        return this;
    }

    public int getCommonsWebpageCrawlRatio() {
        return commonsWebpageCrawlRatio;
    }

    public StaticValue setCommonsWebpageCrawlRatio(int commonsWebpageCrawlRatio) {
        this.commonsWebpageCrawlRatio = commonsWebpageCrawlRatio;
        return this;
    }

    public boolean isNeedEs() {
        return needEs;
    }

    public StaticValue setNeedEs(boolean needEs) {
        this.needEs = needEs;
        return this;
    }

    public String getAjaxDownloader() {
        return ajaxDownloader;
    }

    public void setAjaxDownloader(String ajaxDownloader) {
        this.ajaxDownloader = ajaxDownloader;
    }
}
