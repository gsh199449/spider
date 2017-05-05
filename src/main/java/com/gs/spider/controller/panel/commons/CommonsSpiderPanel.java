package com.gs.spider.controller.panel.commons;

import com.google.gson.Gson;
import com.gs.spider.controller.BaseController;
import com.gs.spider.model.async.State;
import com.gs.spider.model.async.Task;
import com.gs.spider.model.commons.SpiderInfo;
import com.gs.spider.model.commons.Webpage;
import com.gs.spider.model.utils.ResultBundle;
import com.gs.spider.model.utils.ResultListBundle;
import com.gs.spider.service.commons.spider.CommonsSpiderService;
import com.gs.spider.service.commons.spiderinfo.SpiderInfoService;
import com.gs.spider.service.commons.webpage.CommonWebpageService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CommonsSpiderPanel
 *
 * @author Gao Shen
 * @version 16/5/11
 */
@Controller
@RequestMapping("panel/commons")
public class CommonsSpiderPanel extends BaseController {
    private static final Gson gson = new Gson();
    private Logger LOG = LogManager.getLogger(CommonsSpiderPanel.class);
    @Autowired
    private CommonsSpiderService commonsSpiderService;
    @Autowired
    private CommonWebpageService commonWebpageService;
    @Autowired
    private SpiderInfoService spiderInfoService;

    /**
     * 已抓取的网页列表
     *
     * @param query  查询词
     * @param domain 域名
     * @param page   页码
     * @return
     */
    @RequestMapping(value = {"list", ""}, method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(required = false) String query, @RequestParam(required = false) String domain, @RequestParam(defaultValue = "1", required = false) int page) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/list");
        modelAndView.addObject("query", query);
        modelAndView.addObject("page", page);
        modelAndView.addObject("domain", domain);
        if (StringUtils.isNotBlank(query)) {
            modelAndView.addObject("resultBundle", commonWebpageService.searchByQuery(query, 10, page).getResultList());
        } else if (StringUtils.isNotBlank(domain)) {
            modelAndView.addObject("resultBundle", commonWebpageService.getWebpageByDomain(domain, 10, page).getResultList());
        } else {
            modelAndView.addObject("resultBundle", commonWebpageService.listAll(10, page).getResultList());
        }
        return modelAndView;
    }


    /**
     * 域名列表
     *
     * @return
     */
    @RequestMapping(value = "domainList", method = RequestMethod.GET)
    public ModelAndView domainList(@RequestParam(defaultValue = "50", required = false, value = "size") int size) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/domainList");
        modelAndView.addObject("domainList", commonWebpageService.countDomain(size).getResult());
        return modelAndView;
    }

    /**
     * 所有的抓取任务列表
     *
     * @return
     */
    @RequestMapping(value = "tasks", method = RequestMethod.GET)
    public ModelAndView tasks(@RequestParam(required = false, defaultValue = "false") boolean showRunning) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/listTasks");
        ResultListBundle<Task> listBundle;
        if (!showRunning) {
            listBundle = commonsSpiderService.getTaskList(true);
        } else {
            listBundle = commonsSpiderService.getTasksFilterByState(State.RUNNING, true);
        }
        ResultBundle<Long> runningTaskCount = commonsSpiderService.countByState(State.RUNNING);
        modelAndView.addObject("resultBundle", listBundle);
        modelAndView.addObject("runningTaskCount", runningTaskCount.getResult());
        modelAndView.addObject("spiderInfoList", listBundle.getResultList().stream()
                .map(task -> StringEscapeUtils.escapeHtml4(
                        gson.toJson(task.getExtraInfoByKey("spiderInfo")
                        ))
                ).collect(Collectors.toList()));
        return modelAndView;
    }

    /**
     * 编辑爬虫模板
     *
     * @param jsonSpiderInfo json格式的爬虫模板
     * @return
     */
    @RequestMapping(value = "editSpiderInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView editSpiderInfo(String jsonSpiderInfo) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/editSpiderInfo");
        if (StringUtils.isNotBlank(jsonSpiderInfo)) {
            SpiderInfo spiderInfo = gson.fromJson(jsonSpiderInfo, SpiderInfo.class);
            //对可能含有html的字段进行转义
            spiderInfo.setPublishTimeReg(StringEscapeUtils.escapeHtml4(spiderInfo.getPublishTimeReg()));
            spiderInfo.setCategoryReg(StringEscapeUtils.escapeHtml4(spiderInfo.getCategoryReg()));
            spiderInfo.setContentReg(StringEscapeUtils.escapeHtml4(spiderInfo.getContentReg()));
            spiderInfo.setTitleReg(StringEscapeUtils.escapeHtml4(spiderInfo.getTitleReg()));
            spiderInfo.setPublishTimeXPath(StringEscapeUtils.escapeHtml4(spiderInfo.getPublishTimeXPath()));
            spiderInfo.setCategoryXPath(StringEscapeUtils.escapeHtml4(spiderInfo.getCategoryXPath()));
            spiderInfo.setContentXPath(StringEscapeUtils.escapeHtml4(spiderInfo.getContentXPath()));
            spiderInfo.setTitleXPath(StringEscapeUtils.escapeHtml4(spiderInfo.getTitleXPath()));
            for (SpiderInfo.FieldConfig config : spiderInfo.getDynamicFields()) {
                config.setRegex(StringEscapeUtils.escapeHtml4(config.getRegex()));
                config.setXpath(StringEscapeUtils.escapeHtml4(config.getXpath()));
            }
            modelAndView.addObject("spiderInfo", spiderInfo);
            modelAndView.addObject("jsonSpiderInfo", jsonSpiderInfo);
        } else {
            modelAndView.addObject("spiderInfo", new SpiderInfo());
        }
        return modelAndView;
    }

    /**
     * 编辑爬虫模板
     *
     * @param spiderInfoId 爬虫模板id
     * @return
     */
    @RequestMapping(value = "editSpiderInfoById", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView editSpiderInfoById(String spiderInfoId) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/editSpiderInfo");
        SpiderInfo spiderInfo = spiderInfoService.getById(spiderInfoId).getResult();
        //对可能含有html的字段进行转义
        spiderInfo.setPublishTimeReg(StringEscapeUtils.escapeHtml4(spiderInfo.getPublishTimeReg()));
        spiderInfo.setCategoryReg(StringEscapeUtils.escapeHtml4(spiderInfo.getCategoryReg()));
        spiderInfo.setContentReg(StringEscapeUtils.escapeHtml4(spiderInfo.getContentReg()));
        spiderInfo.setTitleReg(StringEscapeUtils.escapeHtml4(spiderInfo.getTitleReg()));
        spiderInfo.setPublishTimeXPath(StringEscapeUtils.escapeHtml4(spiderInfo.getPublishTimeXPath()));
        spiderInfo.setCategoryXPath(StringEscapeUtils.escapeHtml4(spiderInfo.getCategoryXPath()));
        spiderInfo.setContentXPath(StringEscapeUtils.escapeHtml4(spiderInfo.getContentXPath()));
        spiderInfo.setTitleXPath(StringEscapeUtils.escapeHtml4(spiderInfo.getTitleXPath()));
        for (SpiderInfo.FieldConfig config : spiderInfo.getDynamicFields()) {
            config.setRegex(StringEscapeUtils.escapeHtml4(config.getRegex()));
            config.setXpath(StringEscapeUtils.escapeHtml4(config.getXpath()));
        }
        modelAndView.addObject("spiderInfo", spiderInfo);
        modelAndView.addObject("jsonSpiderInfo", gson.toJson(spiderInfo));
        return modelAndView;
    }

    @RequestMapping(value = "listSpiderInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView listSpiderInfo(String domain, @RequestParam(defaultValue = "1", required = false) int page) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/listSpiderInfo");
        if (StringUtils.isBlank(domain)) {
            modelAndView.addObject("spiderInfoList", spiderInfoService.listAll(10, page).getResultList());
        } else {
            modelAndView.addObject("spiderInfoList", spiderInfoService.getByDomain(domain, 10, page).getResultList());
        }
        modelAndView.addObject("page", page);
        modelAndView.addObject("domain", domain);
        return modelAndView;
    }

    @RequestMapping(value = "updateBySpiderInfoID", method = {RequestMethod.GET})
    public ModelAndView updateBySpiderInfoID(@RequestParam(required = false, defaultValue = "") String spiderInfoIdUpdateBy, @RequestParam(required = false, defaultValue = "") String spiderInfoJson) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/updateBySpiderInfoID");
        modelAndView.addObject("spiderInfoJson", spiderInfoJson);
        modelAndView.addObject("spiderInfoIdUpdateBy", spiderInfoIdUpdateBy);
        return modelAndView;
    }

    /**
     * 根据网页id展示网页
     *
     * @param id 网页id
     * @return 展示页
     */
    @RequestMapping(value = "showWebpageById", method = {RequestMethod.GET})
    public ModelAndView showWebpageById(String id) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/showWebpageById");
        modelAndView.addObject("webpage", commonWebpageService.getWebpageById(id).getResult());
        modelAndView.addObject("relatedWebpageList", commonWebpageService.moreLikeThis(id, 15, 1).getResultList());
        return modelAndView;
    }

    /**
     * 获取query的关联信息
     *
     * @param query 查询queryString
     * @param size  结果集数量
     * @return 相关信息
     */
    @RequestMapping(value = "showRelatedInfo", method = {RequestMethod.GET})
    public ModelAndView showRelatedInfo(String query, @RequestParam(required = false, defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/showRelatedInfo");
        Pair<Map<String, List<Terms.Bucket>>, List<Webpage>> result = commonWebpageService.relatedInfo(query, size).getResult();
        String title = "";
        String[] queryArray = query.split(":");
        String field = queryArray[0];
        String queryWord = queryArray[1];
        switch (field) {
            case "keywords":
                title += "关键词:";
                break;
            case "namedEntity.nr":
                title += "人物:";
                break;
            case "namedEntity.ns":
                title += "地点:";
                break;
            case "namedEntity.nt":
                title += "机构:";
                break;
        }
        title += queryWord + "的相关信息";
        modelAndView.addObject("relatedPeople", result.getKey().get("relatedPeople"));
        modelAndView.addObject("title", title);
        modelAndView.addObject("relatedLocation", result.getKey().get("relatedLocation"));
        modelAndView.addObject("relatedInstitution", result.getKey().get("relatedInstitution"));
        modelAndView.addObject("relatedKeywords", result.getKey().get("relatedKeywords"));
        modelAndView.addObject("relatedWebpageList", result.getValue());
        return modelAndView;
    }

    @RequestMapping(value = "listQuartz")
    public String listQuartz(Model model) {
        model.addAttribute("list", commonsSpiderService.listAllQuartzJobs().getResult());
        return "panel/commons/listQuartz";
    }

    @RequestMapping(value = "createQuartz", method = RequestMethod.POST)
    public String createQuartz(String spiderInfoId, int hourInterval, RedirectAttributes redirectAttributes) {
        commonsSpiderService.createQuartzJob(spiderInfoId, hourInterval);
        redirectAttributes.addFlashAttribute("msg", "添加成功");
        return "redirect:/panel/commons/listQuartz";
    }

    @RequestMapping(value = "createQuartz", method = RequestMethod.GET)
    public String createQuartz(String spiderInfoId, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("spiderInfoId", spiderInfoId);
        return "panel/commons/createQuartz";
    }
}
