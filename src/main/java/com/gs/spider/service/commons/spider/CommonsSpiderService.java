package com.gs.spider.service.commons.spider;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gs.spider.gather.async.AsyncGather;
import com.gs.spider.gather.async.quartz.QuartzManager;
import com.gs.spider.gather.async.quartz.WebpageSpiderJob;
import com.gs.spider.gather.commons.CommonSpider;
import com.gs.spider.model.commons.SpiderInfo;
import com.gs.spider.model.commons.Webpage;
import com.gs.spider.model.utils.ResultBundle;
import com.gs.spider.model.utils.ResultBundleBuilder;
import com.gs.spider.model.utils.ResultListBundle;
import com.gs.spider.service.AsyncGatherService;
import com.gs.spider.service.commons.spiderinfo.SpiderInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.management.JMException;
import java.util.List;
import java.util.Map;

/**
 * CommonsSpiderService
 *
 * @author Gao Shen
 * @version 16/4/13
 */
@Component
public class CommonsSpiderService extends AsyncGatherService {
    private final String QUARTZ_JOB_GROUP_NAME = "webpage-spider-job";
    private final String QUARTZ_TRIGGER_GROUP_NAME = "webpage-spider-trigger";
    private final String QUARTZ_TRIGGER_NAME_SUFFIX = "-hours";
    private Logger LOG = LogManager.getLogger(CommonsSpiderService.class);
    @Autowired
    private CommonSpider commonSpider;
    @Autowired
    private ResultBundleBuilder bundleBuilder;
    @Autowired
    private SpiderInfoService spiderInfoService;
    @Autowired
    private QuartzManager quartzManager;
    private Gson gson = new Gson();

    @Autowired
    public CommonsSpiderService(@Qualifier("commonSpider") AsyncGather asyncGather) {
        super(asyncGather);
    }

    /**
     * 启动爬虫
     *
     * @param spiderInfo 爬虫模板信息spiderinfo
     * @return 任务id
     */
    public ResultBundle<String> start(SpiderInfo spiderInfo) {
        //如果id为空则直接存储
        if (StringUtils.isBlank(spiderInfo.getId())) {
            validateSpiderInfo(spiderInfo);
            String spiderInfoId = spiderInfoService.index(spiderInfo).getResult();
            spiderInfo.setId(spiderInfoId);
        } else {
            //如果id不为空则更新这个id的爬虫模板
            spiderInfoService.update(spiderInfo);
        }
        return bundleBuilder.bundle(spiderInfo.toString(), () -> commonSpider.start(spiderInfo));
    }

    /**
     * 启动爬虫
     *
     * @param spiderInfoJson 使用json格式进行序列化的spiderinfo
     * @return 任务id
     */
    public ResultBundle<String> start(String spiderInfoJson) {
        Preconditions.checkArgument(StringUtils.isNotBlank(spiderInfoJson), "爬虫配置模板为空");
        SpiderInfo spiderInfo = gson.fromJson(spiderInfoJson, SpiderInfo.class);
        return start(spiderInfo);
    }

    /**
     * 停止爬虫
     *
     * @param uuid 任务id(爬虫uuid)
     * @return
     */
    public ResultBundle<String> stop(String uuid) {
        return bundleBuilder.bundle(uuid, () -> {
            commonSpider.stop(uuid);
            return "OK";
        });
    }

    /**
     * 删除爬虫
     *
     * @param uuid 爬虫uuid 任务id
     * @return
     */
    public ResultBundle<String> delete(String uuid) {
        return bundleBuilder.bundle(uuid, () -> {
            commonSpider.delete(uuid);
            return "OK";
        });
    }

    /**
     * 删除所有爬虫
     *
     * @return
     */
    public ResultBundle<String> deleteAll() {
        return bundleBuilder.bundle(null, () -> {
            commonSpider.deleteAll();
            return "OK";
        });
    }

    /**
     * 获取爬虫运行时信息
     *
     * @param uuid 爬虫uuid 任务id
     * @return
     */
    public ResultBundle<Map<Object, Object>> runtimeInfo(String uuid, boolean containsExtraInfo) {
        return bundleBuilder.bundle(uuid, () -> commonSpider.getSpiderRuntimeInfo(uuid, containsExtraInfo));
    }

    /**
     * 列出所有爬虫的运行时信息
     *
     * @return
     */
    public ResultBundle<Map<String, Map<Object, Object>>> list(boolean containsExtraInfo) {
        return bundleBuilder.bundle(null, () -> commonSpider.listAllSpiders(containsExtraInfo));
    }

    /**
     * 测试爬虫模板
     *
     * @param spiderInfoJson
     * @return
     */
    public ResultListBundle<Webpage> testSpiderInfo(String spiderInfoJson) {
        SpiderInfo spiderInfo = gson.fromJson(spiderInfoJson, SpiderInfo.class);
        validateSpiderInfo(spiderInfo);
        return bundleBuilder.listBundle(spiderInfoJson, () -> commonSpider.testSpiderInfo(spiderInfo));
    }

    /**
     * 获取忽略url黑名单
     *
     * @return
     */
    public ResultListBundle<String> getIgnoredUrls() {
        return bundleBuilder.listBundle(null, () -> commonSpider.getIgnoredUrls());
    }

    /**
     * 添加忽略url黑名单
     *
     * @param postfix
     */
    public ResultBundle<String> addIgnoredUrl(String postfix) {
        return bundleBuilder.bundle(postfix, () -> {
            commonSpider.addIgnoredUrl(postfix);
            return "OK";
        });
    }

    /**
     * 验证爬虫模板
     *
     * @param spiderInfo 爬虫模板
     */
    private void validateSpiderInfo(SpiderInfo spiderInfo) {
        Preconditions.checkArgument(spiderInfo.getStartURL().size() > 0, "起始地址列表不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(spiderInfo.getDomain()), "domain不可为空");
        Preconditions.checkArgument(!spiderInfo.getDomain().contains("/"), "域名不能包含/");
        Preconditions.checkArgument(spiderInfo.getThread() > 0, "线程数必须大于0");
        Preconditions.checkArgument(StringUtils.isNotBlank(spiderInfo.getSiteName()), "网站名称不可为空");
        Preconditions.checkArgument(spiderInfo.getTimeout() > 1000, "超时时间必须大于1秒");
        if (spiderInfo.getDynamicFields() != null) {
            Preconditions.checkArgument(
                    //每一个动态字段都必须有name,而且正则和xpath不可同时为空
                    spiderInfo.getDynamicFields().stream()
                            .filter(fieldConfig ->
                                    StringUtils.isBlank(fieldConfig.getName()) ||
                                            (StringUtils.isBlank(fieldConfig.getRegex()) && StringUtils.isBlank(fieldConfig.getXpath()))
                            )
                            .count() == 0,
                    "动态字段配置含有无效配置,每一个动态字段都必须有name,而且正则和xpath不可同时为空,请检查");
        }
    }

    /**
     * 根据爬虫模板ID批量启动任务
     *
     * @param spiderInfoIdList 爬虫模板ID列表
     * @return 任务id列表
     */
    public ResultListBundle<String> startAll(List<String> spiderInfoIdList) {
        return bundleBuilder.listBundle(spiderInfoIdList.toString(), () -> {
            List<String> taskIdList = Lists.newArrayList();
            for (String id : spiderInfoIdList) {
                try {
                    SpiderInfo info = spiderInfoService.getById(id).getResult();
                    String taskId = commonSpider.start(info);
                    taskIdList.add(taskId);
                } catch (JMException e) {
                    LOG.error("启动任务ID{}出错，{}", id, e);
                }
            }
            return taskIdList;
        });
    }

    /**
     * 创建定时任务
     *
     * @param spiderInfoId  爬虫模板id
     * @param hoursInterval 每几小时运行一次
     */
    public ResultBundle<String> createQuartzJob(String spiderInfoId, int hoursInterval) {
        SpiderInfo spiderInfo = spiderInfoService.getById(spiderInfoId).getResult();
        Map<String, Object> data = Maps.newHashMap();
        data.put("spiderInfo", spiderInfo);
        data.put("commonsSpiderService", this);
        quartzManager.addJob(spiderInfo.getId(), QUARTZ_JOB_GROUP_NAME,
                String.valueOf(hoursInterval) + "-" + spiderInfo.getId() + QUARTZ_TRIGGER_NAME_SUFFIX, QUARTZ_TRIGGER_GROUP_NAME
                , WebpageSpiderJob.class, data, hoursInterval);
        return bundleBuilder.bundle(spiderInfoId, () -> "OK");
    }

    public ResultBundle<Map<String, Triple<SpiderInfo, JobKey, Trigger>>> listAllQuartzJobs() {
        Map<String, Triple<SpiderInfo, JobKey, Trigger>> result = Maps.newHashMap();
        for (JobKey jobKey : quartzManager.listAll(QUARTZ_JOB_GROUP_NAME)) {
            Pair<JobDetail, Trigger> pair = quartzManager.findInfo(jobKey);
            SpiderInfo spiderInfo = ((SpiderInfo) pair.getLeft().getJobDataMap().get("spiderInfo"));
            result.put(spiderInfo.getId(), Triple.of(spiderInfo, jobKey, pair.getRight()));
        }
        return bundleBuilder.bundle("", () -> result);
    }

    public ResultBundle<String> removeQuartzJob(String spiderInfoId) {
        quartzManager.removeJob(JobKey.jobKey(spiderInfoId, QUARTZ_JOB_GROUP_NAME));
        return bundleBuilder.bundle(spiderInfoId, () -> "OK");
    }

    public ResultBundle<String> checkQuartzJob(String spiderInfoId) {
        try {
            Pair<JobDetail, Trigger> pair = quartzManager.findInfo(JobKey.jobKey(spiderInfoId, QUARTZ_JOB_GROUP_NAME));
            SpiderInfo spiderInfo = spiderInfoService.getById(spiderInfoId).getResult();
            if (pair == null && spiderInfo != null) {
                return bundleBuilder.bundle(spiderInfoId, () -> "true");
            } else {
                return bundleBuilder.bundle(spiderInfoId, () -> "爬虫模板不存在或该爬虫模板已添加至定时任务");
            }
        } catch (Exception e) {
            return bundleBuilder.bundle(spiderInfoId, e::getLocalizedMessage);
        }

    }

    public String exportQuartz() {
        Map<String, Long> result = Maps.newHashMap();
        for (JobKey jobKey : quartzManager.listAll(QUARTZ_JOB_GROUP_NAME)) {
            Pair<JobDetail, Trigger> pair = quartzManager.findInfo(jobKey);
            long hours = ((SimpleTrigger) ((SimpleScheduleBuilder) pair.getRight().getScheduleBuilder()).build()).getRepeatInterval() / DateBuilder.MILLISECONDS_IN_HOUR;
            String name = ((SpiderInfo) pair.getLeft().getJobDataMap().get("spiderInfo")).getId();
            result.put(name, hours);
        }
        return new Gson().toJson(result);
    }

    public void importQuartz(String json) {
        Map<String, Integer> result = new Gson().fromJson(json, new TypeToken<Map<String, Integer>>() {
        }.getType());
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            createQuartzJob(entry.getKey(), entry.getValue());
        }
    }

}
