package com.gs.spider.gather.async.quartz;

import com.gs.spider.model.commons.SpiderInfo;
import com.gs.spider.service.commons.spider.CommonsSpiderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by gaoshen on 2017/1/18.
 */
@DisallowConcurrentExecution
public class WebpageSpiderJob extends QuartzJobBean {
    private Logger LOG = LogManager.getLogger(WebpageSpiderJob.class);
    private SpiderInfo spiderInfo;
    private CommonsSpiderService commonsSpiderService;

    public WebpageSpiderJob setCommonsSpiderService(CommonsSpiderService commonsSpiderService) {
        this.commonsSpiderService = commonsSpiderService;
        return this;
    }

    public WebpageSpiderJob setSpiderInfo(SpiderInfo spiderInfo) {
        this.spiderInfo = spiderInfo;
        return this;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("开始定时网页采集任务，网站：{}，模板ID：{}", spiderInfo.getSiteName(), spiderInfo.getId());
        String uuid = commonsSpiderService.start(spiderInfo).getResult();
        LOG.info("定时网页采集任务完成，网站：{}，模板ID：{},任务ID：{}", spiderInfo.getSiteName(), spiderInfo.getId(), uuid);
    }
}
