package com.gs.spider.service;

import com.gs.spider.gather.async.AsyncGather;
import com.gs.spider.model.async.State;
import com.gs.spider.model.async.Task;
import com.gs.spider.model.utils.MySupplier;
import com.gs.spider.model.utils.ResultBundle;
import com.gs.spider.model.utils.ResultBundleBuilder;
import com.gs.spider.model.utils.ResultListBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * AsyncGatherService
 * 异步数据抓取服务,提供任务管理基础方法
 *
 * @author Gao Shen
 * @version 16/2/23
 */
@Component
public class AsyncGatherService {
    protected AsyncGather asyncGather;
    @Autowired
    protected ResultBundleBuilder bundleBuilder;
    private Logger LOG = LogManager.getLogger(AsyncGatherService.class);

    public AsyncGatherService(AsyncGather asyncGather) {
        this.asyncGather = asyncGather;
    }

    public AsyncGatherService() {
    }

    /**
     * 获取task列表,包括正在运行和已经完成的task
     *
     * @return
     */
    public ResultListBundle<Task> getTaskList(boolean containsExtraInfo) {
        MySupplier<List<Task>> supplier = () -> new LinkedList<>(asyncGather.getTasks(containsExtraInfo));
        return bundleBuilder.listBundle(null, supplier);
    }

    /**
     * 根据taskid获取task
     *
     * @param taskId
     * @return task
     */
    public ResultBundle<Task> getTaskById(String taskId, boolean containsExtraInfo) {
        MySupplier<Task> supplier = () -> asyncGather.getTaskById(taskId, containsExtraInfo);
        return bundleBuilder.bundle(null, supplier);
    }

    /**
     * 获取指定task当前已经抓取的文章数
     *
     * @param taskId 任务ID
     * @return task当前已经抓取的文章数
     */
    public ResultBundle<Integer> getTaskCount(String taskId) {
        MySupplier<Integer> supplier = () -> asyncGather.getTaskCount(taskId);
        return bundleBuilder.bundle(null, supplier);
    }

    /**
     * 获取异步抓取长连接服务器端口号
     *
     * @return 端口号
     */
    public ResultBundle<Integer> getLongConnectionPort() {
        MySupplier<Integer> supplier = () -> asyncGather.getLongConnectionPort();
        return bundleBuilder.bundle(null, supplier);
    }

    /**
     * 根据taskId删除任务
     *
     * @param taskId 任务ID
     * @return 成功返回OK!
     */
    public ResultBundle<String> deleteTaskById(String taskId) {
        MySupplier<String> supplier = () -> {
            asyncGather.deleteTaskById(taskId);
            return "OK!";
        };
        return bundleBuilder.bundle(null, supplier);
    }

    /**
     * 统计指定任务状态的任务数量
     *
     * @param state 任务状态
     * @return 本状态的任务数
     */
    public ResultBundle<Long> countByState(State state) {
        return bundleBuilder.bundle(state.name(), () -> asyncGather.countByState(state));
    }

    /**
     * 获取任务列表,通过状态过滤
     *
     * @param state 任务状态
     * @return
     */
    public ResultListBundle<Task> getTasksFilterByState(State state, boolean containsExtraInfo) {
        return bundleBuilder.listBundle(state.name(), () -> asyncGather.getTasksFilterByState(state, containsExtraInfo));
    }

    /**
     * 获取任务列表,通过时间状态过滤
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public ResultListBundle<Task> getTasksFilterByTime(long start, long end, boolean containsExtraInfo) {
        return bundleBuilder.listBundle("start:" + start + ",end:" + end, () -> asyncGather.getTasksFilterByTime(start, end, containsExtraInfo));
    }
}
