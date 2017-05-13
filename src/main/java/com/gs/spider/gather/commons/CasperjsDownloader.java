package com.gs.spider.gather.commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;

/**
 * CasperjsDownloader
 *
 * @author Gao Shen
 * @version 16/7/1
 */
@Component
public class CasperjsDownloader extends AbstractDownloader {
    private final static Logger LOG = LogManager.getLogger(CasperjsDownloader.class);
    @Autowired
    private Casperjs casperjs;

    @Override
    public Page download(Request request, Task task) {
        String html = null;
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }
        try {
            html = casperjs.gatherHtml(new com.gs.spider.model.commons.Request(request.getUrl(), true));
        } catch (Exception e) {
            if (site.getCycleRetryTimes() > 0) {
                return addToCycleRetry(request, site);
            }
            request.putExtra("EXCEPTION", e);
            onError(request);
            return null;
        }
        Page page = new Page();
        page.setRawText(html);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        onSuccess(request);
        return page;
    }

    @Override
    public void setThread(int threadNum) {
    }
}
