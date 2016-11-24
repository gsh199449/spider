package com.gs.spider.gather.async;

import com.gs.spider.model.async.State;
import com.gs.spider.model.async.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * AsyncGather
 * 异步抓取器基类,提供任务管理功能
 *
 * @author Gao Shen
 * @version 16/2/23
 */
public class AsyncGather {
    protected TaskManager taskManager;
    protected int longConnectionPort;
    private Logger LOG = LogManager.getLogger(AsyncGather.class);

    public AsyncGather() {
    }

    /**
     * 获取所有Task,包括已经完成的和未完成的
     *
     * @param containsExtraInfo 是否显示额外信息
     * @return
     */
    public Collection<Task> getTasks(boolean containsExtraInfo) {
        return taskManager.getTasks(containsExtraInfo);
    }

    /**
     * 获取任务列表,通过状态过滤
     *
     * @param state 任务状态
     * @return
     */
    public Collection<Task> getTasksFilterByState(State state, boolean containsExtraInfo) {
        return taskManager.getTasks(containsExtraInfo).stream().filter(task -> task.getState() == state).collect(Collectors.toList());
    }

    /**
     * 获取任务列表,通过时间状态过滤
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public Collection<Task> getTasksFilterByTime(long start, long end, boolean containsExtraInfo) {
        return taskManager.getTasks(containsExtraInfo).stream().filter(task -> task.getTime() > start && task.getTime() < end).collect(Collectors.toList());
    }

    /**
     * 根据任务ID获取单个任务信息
     *
     * @param taskId 任务ID
     * @return
     */
    public Task getTaskById(String taskId, boolean containsExtraInfo) {
        return taskManager.getTaskById(taskId, containsExtraInfo);
    }

    /**
     * 根据任务ID获取当前任务已经获取的数据条数
     *
     * @param taskId 任务ID
     * @return
     */
    public int getTaskCount(String taskId) {
        return taskManager.getTaskCount(taskId);
    }

    /**
     * 根据taskId删除任务
     *
     * @param taskId 任务ID
     */
    public void deleteTaskById(String taskId) {
        taskManager.deleteTask(taskId);
    }

    /**
     * 获取长连接服务器端口
     *
     * @return
     */
    public int getLongConnectionPort() {
        return this.longConnectionPort;
    }

    /**
     * 统计指定状态的任务数
     *
     * @param state 指定的任务状态
     * @return 指定任务状态的任务数量
     */
    public long countByState(State state) {
        return taskManager.countByState(state);
    }
}
