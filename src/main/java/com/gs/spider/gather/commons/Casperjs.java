package com.gs.spider.gather.commons;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.gs.spider.model.commons.Request;
import com.gs.spider.utils.HttpClientUtil;
import com.gs.spider.utils.StaticValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Casperjs
 *
 * @author Gao Shen
 * @version 16/4/2
 */
@Component
public class Casperjs {
    private static Logger LOG = LogManager.getLogger(Casperjs.class);
    private static Gson gson = new Gson();
    @Autowired
    private HttpClientUtil httpUtils;
    @Autowired
    private StaticValue staticValue;

    /**
     * 抓取html
     *
     * @param request 请求实体
     * @param url     casper url
     * @return
     * @throws IOException
     */
    private String gatherHtml(Request request, String url) throws IOException {
        Preconditions.checkArgument(request.getUrl().startsWith("http"), "url必须以http开头,当前url:%s", request.getUrl());
        Fetch fetch = new Fetch().setUrl(request.getUrl());
        String json = httpUtils.post(url, gson.toJson(fetch));
        json = new String(json.getBytes("iso8859-1"), "utf8");
        return new JsonParser().parse(json).getAsJsonObject().get("content").getAsString();
    }

    /**
     * 抓取网页html
     *
     * @param request 请求实体
     * @return
     * @throws IOException
     */
    public String gatherHtml(Request request) throws IOException {
        return gatherHtml(request, staticValue.getAjaxDownloader() + "html");
    }


    public class Fetch {
        private String proxy = "";
        private int jsViewportWidth = 1024;
        private int jsViewportHeight = 1024;
        private boolean loadImages = false;
        private int timeout = 5;
        private String url;
        private String method = "get";
        private String data = "";
        private Map<String, String> headers;
        private String jsRunAt;
        private String jsScript;

        public String getProxy() {
            return proxy;
        }

        public Fetch setProxy(String proxy) {
            this.proxy = proxy;
            return this;
        }

        public int getJsViewportWidth() {
            return jsViewportWidth;
        }

        public Fetch setJsViewportWidth(int jsViewportWidth) {
            this.jsViewportWidth = jsViewportWidth;
            return this;
        }

        public int getJsViewportHeight() {
            return jsViewportHeight;
        }

        public Fetch setJsViewportHeight(int jsViewportHeight) {
            this.jsViewportHeight = jsViewportHeight;
            return this;
        }

        public boolean isLoadImages() {
            return loadImages;
        }

        public Fetch setLoadImages(boolean loadImages) {
            this.loadImages = loadImages;
            return this;
        }

        public int getTimeout() {
            return timeout;
        }

        public Fetch setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public Fetch setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getMethod() {
            return method;
        }

        public Fetch setMethod(String method) {
            this.method = method;
            return this;
        }

        public String getData() {
            return data;
        }

        public Fetch setData(String data) {
            this.data = data;
            return this;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Fetch setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public String getJsRunAt() {
            return jsRunAt;
        }

        public Fetch setJsRunAt(String jsRunAt) {
            this.jsRunAt = jsRunAt;
            return this;
        }

        public String getJsScript() {
            return jsScript;
        }

        public Fetch setJsScript(String jsScript) {
            this.jsScript = jsScript;
            return this;
        }
    }
}
