package com.gs.spider.model.commons;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * 网页抽取模板
 *
 * @author Gao Shen
 * @version 16/4/12
 */
public class SpiderInfo {
    /**
     * 使用多少抓取线程
     */
    private int thread = 1;
    /**
     * 失败的网页重试次数
     */
    private int retry = 2;
    /**
     * 抓取每个网页睡眠时间
     */
    private int sleep = 0;
    /**
     * 最大抓取网页数量,0代表不限制
     */
    private int maxPageGather = 10;
    /**
     * HTTP链接超时时间
     */
    private int timeout = 5000;
    /**
     * 网站权重
     */
    private int priority;
    /**
     * 是否只抓取首页
     */
    private boolean gatherFirstPage = false;
    /**
     * 抓取模板id
     */
    private String id;
    /**
     * 网站名称
     */
    private String siteName;
    /**
     * 域名
     */
    private String domain;
    /**
     * 起始链接
     */
    private List<String> startURL;
    /**
     * 正文正则表达式
     */
    private String contentReg;
    /**
     * 正文Xpath
     */
    private String contentXPath;
    /**
     * 标题正则
     */
    private String titleReg;
    /**
     * 标题xpath
     */
    private String titleXPath;
    /**
     * 分类信息正则
     */
    private String categoryReg;
    /**
     * 分类信息XPath
     */
    private String categoryXPath;
    /**
     * 默认分类
     */
    private String defaultCategory;
    /**
     * url正则
     */
    private String urlReg;
    /**
     * 编码
     */
    private String charset;
    /**
     * 发布时间xpath
     */
    private String publishTimeXPath;
    /**
     * 发布时间正则
     */
    private String publishTimeReg;
    /**
     * 发布时间模板
     */
    private String publishTimeFormat;
    /**
     * 回调url
     */
    private List<String> callbackURL;
    /**
     * 是否进行nlp处理
     */
    private boolean doNLP = true;
    /**
     * 网页必须有标题
     */
    private boolean needTitle = false;
    /**
     * 网页必须有正文
     */
    private boolean needContent = false;
    /**
     * 网页必须有发布时间
     */
    private boolean needPublishTime = false;
    /**
     * 动态字段列表
     */
    private List<FieldConfig> dynamicFields = Lists.newLinkedList();
    /**
     * 静态字段
     */
    private List<StaticField> staticFields = Lists.newArrayList();
    /**
     * 语言,用于配置发布时间
     */
    private String lang;
    /**
     * 国家,用于配置发布时间
     */
    private String country;
    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 5.2) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";
    /**
     * 是否保存网页快照,默认保存
     */
    private boolean saveCapture = true;
    /**
     * 是否是ajax网站,如果是则使用casperjs下载器
     */
    private boolean ajaxSite = false;
    /**
     * 自动探测发布时间
     */
    private boolean autoDetectPublishDate = false;
    private String proxyHost;
    private int proxyPort;
    private String proxyUsername;
    private String proxyPassword;

    public int getThread() {
        return thread;
    }

    public SpiderInfo setThread(int thread) {
        this.thread = thread;
        return this;
    }

    public int getRetry() {
        return retry;
    }

    public SpiderInfo setRetry(int retry) {
        this.retry = retry;
        return this;
    }

    public int getSleep() {
        return sleep;
    }

    public SpiderInfo setSleep(int sleep) {
        this.sleep = sleep;
        return this;
    }

    public int getMaxPageGather() {
        return maxPageGather;
    }

    public SpiderInfo setMaxPageGather(int maxPageGather) {
        this.maxPageGather = maxPageGather;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public SpiderInfo setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public SpiderInfo setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public boolean isGatherFirstPage() {
        return gatherFirstPage;
    }

    public SpiderInfo setGatherFirstPage(boolean gatherFirstPage) {
        this.gatherFirstPage = gatherFirstPage;
        return this;
    }

    public String getId() {
        return id;
    }

    public SpiderInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getSiteName() {
        return siteName;
    }

    public SpiderInfo setSiteName(String siteName) {
        this.siteName = siteName;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public SpiderInfo setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public List<String> getStartURL() {
        return startURL;
    }

    public SpiderInfo setStartURL(List<String> startURL) {
        this.startURL = startURL;
        return this;
    }

    public String getContentReg() {
        return contentReg;
    }

    public SpiderInfo setContentReg(String contentReg) {
        this.contentReg = contentReg;
        return this;
    }

    public String getContentXPath() {
        return contentXPath;
    }

    public SpiderInfo setContentXPath(String contentXPath) {
        this.contentXPath = contentXPath;
        return this;
    }

    public String getTitleReg() {
        return titleReg;
    }

    public SpiderInfo setTitleReg(String titleReg) {
        this.titleReg = titleReg;
        return this;
    }

    public String getTitleXPath() {
        return titleXPath;
    }

    public SpiderInfo setTitleXPath(String titleXPath) {
        this.titleXPath = titleXPath;
        return this;
    }

    public String getCategoryReg() {
        return categoryReg;
    }

    public SpiderInfo setCategoryReg(String categoryReg) {
        this.categoryReg = categoryReg;
        return this;
    }

    public String getCategoryXPath() {
        return categoryXPath;
    }

    public SpiderInfo setCategoryXPath(String categoryXPath) {
        this.categoryXPath = categoryXPath;
        return this;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public SpiderInfo setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
        return this;
    }

    public String getUrlReg() {
        return urlReg;
    }

    public SpiderInfo setUrlReg(String urlReg) {
        this.urlReg = urlReg;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public SpiderInfo setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getPublishTimeXPath() {
        return publishTimeXPath;
    }

    public SpiderInfo setPublishTimeXPath(String publishTimeXPath) {
        this.publishTimeXPath = publishTimeXPath;
        return this;
    }

    public String getPublishTimeReg() {
        return publishTimeReg;
    }

    public SpiderInfo setPublishTimeReg(String publishTimeReg) {
        this.publishTimeReg = publishTimeReg;
        return this;
    }

    public String getPublishTimeFormat() {
        return publishTimeFormat;
    }

    public SpiderInfo setPublishTimeFormat(String publishTimeFormat) {
        this.publishTimeFormat = publishTimeFormat;
        return this;
    }

    public List<String> getCallbackURL() {
        return callbackURL;
    }

    public SpiderInfo setCallbackURL(List<String> callbackURL) {
        this.callbackURL = callbackURL;
        return this;
    }

    public boolean isDoNLP() {
        return doNLP;
    }

    public SpiderInfo setDoNLP(boolean doNLP) {
        this.doNLP = doNLP;
        return this;
    }

    public boolean isNeedTitle() {
        return needTitle;
    }

    public SpiderInfo setNeedTitle(boolean needTitle) {
        this.needTitle = needTitle;
        return this;
    }

    public boolean isNeedContent() {
        return needContent;
    }

    public SpiderInfo setNeedContent(boolean needContent) {
        this.needContent = needContent;
        return this;
    }

    public boolean isNeedPublishTime() {
        return needPublishTime;
    }

    public SpiderInfo setNeedPublishTime(boolean needPublishTime) {
        this.needPublishTime = needPublishTime;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public SpiderInfo setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public SpiderInfo setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public SpiderInfo setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public List<FieldConfig> getDynamicFields() {
        return dynamicFields;
    }

    public SpiderInfo setDynamicFields(List<FieldConfig> dynamicFields) {
        this.dynamicFields = dynamicFields;
        return this;
    }

    public List<StaticField> getStaticFields() {
        return staticFields;
    }

    public SpiderInfo setStaticFields(List<StaticField> staticFields) {
        this.staticFields = staticFields;
        return this;
    }

    public boolean isSaveCapture() {
        return saveCapture;
    }

    public SpiderInfo setSaveCapture(boolean saveCapture) {
        this.saveCapture = saveCapture;
        return this;
    }

    public boolean isAutoDetectPublishDate() {
        return autoDetectPublishDate;
    }

    public SpiderInfo setAutoDetectPublishDate(boolean autoDetectPublishDate) {
        this.autoDetectPublishDate = autoDetectPublishDate;
        return this;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public SpiderInfo setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public SpiderInfo setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public SpiderInfo setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
        return this;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public SpiderInfo setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

    public boolean isAjaxSite() {
        return ajaxSite;
    }

    public SpiderInfo setAjaxSite(boolean ajaxSite) {
        this.ajaxSite = ajaxSite;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SpiderInfo that = (SpiderInfo) o;

        return new EqualsBuilder()
                .append(getThread(), that.getThread())
                .append(getRetry(), that.getRetry())
                .append(getSleep(), that.getSleep())
                .append(getMaxPageGather(), that.getMaxPageGather())
                .append(getTimeout(), that.getTimeout())
                .append(getPriority(), that.getPriority())
                .append(isGatherFirstPage(), that.isGatherFirstPage())
                .append(isDoNLP(), that.isDoNLP())
                .append(isNeedTitle(), that.isNeedTitle())
                .append(isNeedContent(), that.isNeedContent())
                .append(isNeedPublishTime(), that.isNeedPublishTime())
                .append(getSiteName(), that.getSiteName())
                .append(getDomain(), that.getDomain())
                .append(getStartURL(), that.getStartURL())
                .append(getContentReg(), that.getContentReg())
                .append(getContentXPath(), that.getContentXPath())
                .append(getTitleReg(), that.getTitleReg())
                .append(getTitleXPath(), that.getTitleXPath())
                .append(getCategoryReg(), that.getCategoryReg())
                .append(getCategoryXPath(), that.getCategoryXPath())
                .append(getDefaultCategory(), that.getDefaultCategory())
                .append(getUrlReg(), that.getUrlReg())
                .append(getCharset(), that.getCharset())
                .append(getPublishTimeXPath(), that.getPublishTimeXPath())
                .append(getPublishTimeReg(), that.getPublishTimeReg())
                .append(getPublishTimeFormat(), that.getPublishTimeFormat())
                .append(getLang(), that.getLang())
                .append(getCountry(), that.getCountry())
                .append(getUserAgent(), that.getUserAgent())
                .append(getDynamicFields(), that.getDynamicFields())
                .append(getStaticFields(), that.getStaticFields())
                .append(isSaveCapture(), that.isSaveCapture())
                .append(isAutoDetectPublishDate(), that.isAutoDetectPublishDate())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getThread())
                .append(getRetry())
                .append(getSleep())
                .append(getMaxPageGather())
                .append(getTimeout())
                .append(getPriority())
                .append(isGatherFirstPage())
                .append(getSiteName())
                .append(getDomain())
                .append(getStartURL())
                .append(getContentReg())
                .append(getContentXPath())
                .append(getTitleReg())
                .append(getTitleXPath())
                .append(getCategoryReg())
                .append(getCategoryXPath())
                .append(getDefaultCategory())
                .append(getUrlReg())
                .append(getCharset())
                .append(getPublishTimeXPath())
                .append(getPublishTimeReg())
                .append(getPublishTimeFormat())
                .append(isDoNLP())
                .append(isNeedTitle())
                .append(isNeedContent())
                .append(isNeedPublishTime())
                .append(getLang())
                .append(getCountry())
                .append(getUserAgent())
                .append(getDynamicFields())
                .append(getStaticFields())
                .append(isSaveCapture())
                .append(isAutoDetectPublishDate())
                .toHashCode();
    }

    public class StaticField {
        private String name;
        private String value;

        public StaticField(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public StaticField setName(String name) {
            this.name = name;
            return this;
        }

        public String getValue() {
            return value;
        }

        public StaticField setValue(String value) {
            this.value = value;
            return this;
        }
    }

    public class FieldConfig {
        private String regex;
        private String xpath;
        private String name;
        private boolean need = false;

        public FieldConfig(String regex, String xpath, String name, boolean need) {
            this.regex = regex;
            this.xpath = xpath;
            this.name = name;
            this.need = need;
        }

        public FieldConfig() {
        }

        public String getRegex() {
            return regex;
        }

        public FieldConfig setRegex(String regex) {
            this.regex = regex;
            return this;
        }

        public String getXpath() {
            return xpath;
        }

        public FieldConfig setXpath(String xpath) {
            this.xpath = xpath;
            return this;
        }

        public String getName() {
            return name;
        }

        public FieldConfig setName(String name) {
            this.name = name;
            return this;
        }

        public boolean isNeed() {
            return need;
        }

        public FieldConfig setNeed(boolean need) {
            this.need = need;
            return this;
        }
    }
}
