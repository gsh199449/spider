package com.gs.spider.gather.commons;

import com.gs.spider.model.async.Task;
import com.gs.spider.model.commons.SpiderInfo;
import us.codecraft.webmagic.Page;

/**
 * PageConsumer
 *
 * @author Gao Shen
 * @version 16/7/8
 */
@FunctionalInterface
public interface PageConsumer {
    void accept(Page page, SpiderInfo info, Task task);
}
