package com.gs.spider.gather.async.quartz;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created by gaoshen on 2017/1/18.
 */
@Component
public class QuartzManager {
    @Autowired
    private Scheduler scheduler;

    /**
     * @param jobName      任务名
     * @param jobGroupName 任务组名
     * @param jobClass     任务
     * @param hours        时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     * @Title: QuartzManager.java
     */
    public Pair<TriggerKey, JobKey> addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<? extends Job> jobClass, Map<String, Object> data, int hours) {
        try {
            JobDetail jobDetail = JobBuilder.newJob()
                    .ofType(jobClass)
                    .usingJobData(new JobDataMap(data))
                    .withIdentity(jobName, jobGroupName).build();// 任务名，任务组，任务执行类
            // 触发器
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobName, jobGroupName)
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(hours))
                    .build();// 触发器名,触发器组
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);
            return Pair.of(trigger.getKey(), jobDetail.getKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Pair<JobDetail, Trigger> findInfo(JobKey jobKey) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
            return Pair.of(jobDetail, trigger);
        } catch (Exception e) {
            return null;
        }
    }

    public Set<JobKey> listAll(String jobGroup) {
        try {
            return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Sets.newConcurrentHashSet();
    }

    /**
     * @Description: 移除一个任务
     * @Title: QuartzManager.java
     */
    public void removeJob(JobKey jobKey) {
        try {
            TriggerKey triggerKey = scheduler.getTriggersOfJob(jobKey).get(0).getKey();
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     * @Title: QuartzManager.java
     */
    public void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     * @Title: QuartzManager.java
     */
    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
