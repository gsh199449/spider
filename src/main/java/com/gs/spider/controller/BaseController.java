package com.gs.spider.controller;

import com.gs.spider.model.utils.ResultBundle;
import com.gs.spider.model.utils.ResultBundleBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by gsh199449 on 2016/11/24.
 */
@Controller
public class BaseController {
    private final static Logger LOG = LogManager.getLogger(BaseController.class);
    @Autowired
    private ResultBundleBuilder bundleBuilder;

    /**
     * 异常页面控制
     *
     * @param runtimeException
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public
    @ResponseBody
    ResultBundle<String> runtimeExceptionHandler(RuntimeException runtimeException) {
        return new ResultBundle<>("", 0, false, runtimeException.toString());
    }
}

