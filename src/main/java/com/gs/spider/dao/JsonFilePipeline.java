package com.gs.spider.dao;

import com.google.gson.Gson;
import com.gs.spider.model.commons.Webpage;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.IOException;

/**
 * Created by gsh199449 on 2016/12/8.
 */
@Component
public class JsonFilePipeline implements Pipeline {
    private final static Logger LOG = LogManager.getLogger(JsonFilePipeline.class);
    private final static Gson gson = new Gson();

    @Override
    public void process(ResultItems resultItems, Task task) {
        Webpage webpage = CommonWebpagePipeline.convertResultItems2Webpage(resultItems);
        try {
            FileUtils.writeStringToFile(
                    new File("gather_platform_data/" + webpage.getSpiderUUID() + ".json"),
                    gson.toJson(webpage) + "\n",
                    true);
        } catch (IOException e) {
            LOG.error("序列化网页信息出错,{}", e.getLocalizedMessage());
        }
    }
}

