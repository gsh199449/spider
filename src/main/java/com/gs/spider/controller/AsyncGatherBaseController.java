package com.gs.spider.controller;

import com.gs.spider.model.async.State;
import com.gs.spider.model.async.Task;
import com.gs.spider.model.utils.ResultBundle;
import com.gs.spider.model.utils.ResultListBundle;
import com.gs.spider.service.AsyncGatherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * AsyncGatherBaseController
 * 异步抓取器的Controller
 *
 * @author Gao Shen
 * @version 16/2/23
 */
public class AsyncGatherBaseController extends BaseController {
    private AsyncGatherService asyncGatherService;
    private Logger LOG = LogManager.getLogger(AsyncGatherBaseController.class);

    public AsyncGatherBaseController(AsyncGatherService asyncGatherService) {
        this.asyncGatherService = asyncGatherService;
    }

    /**
     * 列出所有任务
     *
     * @return 0表示正在进行 1表示已经完成
     * @throws IOException
     */
    @RequestMapping(value = "listTasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultListBundle<Task> listTasks(@RequestParam(value = "containsExtraInfo", required = false, defaultValue = "false") boolean containsExtraInfo) throws IOException {
        return asyncGatherService.getTaskList(containsExtraInfo);
    }

    /**
     * 根据id获取task
     *
     * @param taskId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "getTaskById", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBundle<Task> getTaskById(String taskId, @RequestParam(value = "containsExtraInfo", required = false, defaultValue = "true") boolean containsExtraInfo) throws IOException {
        return asyncGatherService.getTaskById(taskId, containsExtraInfo);
    }

    /**
     * 获取异步抓取长连接服务器端口号
     *
     * @return
     */
    @RequestMapping(value = "getLongConnectionPort", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBundle<Integer> getLongConnectionPort() throws IOException {
        return asyncGatherService.getLongConnectionPort();
    }

    /**
     * 获取当前task已经抓取的文章数
     *
     * @param taskId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "getTaskCount", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBundle<Integer> getTaskCount(String taskId) throws IOException {
        return asyncGatherService.getTaskCount(taskId);
    }

    /**
     * 根据taskId删除任务
     *
     * @param taskId 任务ID
     * @return 成功返回OK!
     */
    @RequestMapping(value = "deleteTaskById", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBundle<String> deleteTaskById(String taskId) {
        return asyncGatherService.deleteTaskById(taskId);
    }

    /**
     * 获取任务列表,通过状态过滤
     *
     * @param state 任务状态
     * @return
     */
    @RequestMapping(value = "getTasksFilterByState", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultListBundle<Task> getTasksFilterByState(State state, @RequestParam(value = "containsExtraInfo", required = false, defaultValue = "false") boolean containsExtraInfo) {
        return asyncGatherService.getTasksFilterByState(state, containsExtraInfo);
    }

    /**
     * 获取任务列表,通过时间状态过滤
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    @RequestMapping(value = "getTasksFilterByTime", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultListBundle<Task> getTasksFilterByTime(long start, long end, @RequestParam(value = "containsExtraInfo", required = false, defaultValue = "false") boolean containsExtraInfo) {
        return asyncGatherService.getTasksFilterByTime(start, end, containsExtraInfo);
    }
}
