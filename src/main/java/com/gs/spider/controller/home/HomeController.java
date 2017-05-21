package com.gs.spider.controller.home;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gs.spider.controller.BaseController;
import com.gs.spider.utils.AppInfo;

/**
 * Created by gsh199449 on 2016/11/24.
 */
@Controller
@RequestMapping("/")
public class HomeController extends BaseController {
    private final static Logger LOG = LogManager.getLogger(HomeController.class);

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public ModelAndView home() {
    	ModelAndView modelAndView = new ModelAndView("panel/welcome/welcome");
    	modelAndView.addObject("appName", AppInfo.APP_NAME)
    		.addObject("appVersion", AppInfo.APP_VERSION)
    		.addObject("onlineDocumentation",AppInfo.ONLINE_DOCUMENTATION);
        return modelAndView;
    }

}

