package com.gs.spider.gather.commons;

import com.google.common.collect.Sets;
import com.gs.spider.utils.StaticValue;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ContentLengthLimitHttpClientDownloader
 *
 * @author Gao Shen
 * @version 16/7/17
 */
@Component
public class ContentLengthLimitHttpClientDownloader extends HttpClientDownloader {
    private final static Logger LOG = LogManager.getLogger(ContentLengthLimitHttpClientDownloader.class);
    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();
    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();
    @Autowired
    private StaticValue staticValue;

    @Override
    protected String getContent(String charset, HttpResponse httpResponse) throws IOException {
        if (charset == null) {
            long contentLength = httpResponse.getEntity().getContentLength();
            if (httpResponse.getFirstHeader("Content-Type") != null && !httpResponse.getFirstHeader("Content-Type").getValue().toLowerCase().contains("text/html")) {
                throw new IllegalArgumentException("本链接为非HTML内容,不下载,内容类型为:" + httpResponse.getFirstHeader("Content-Type"));
            } else if (contentLength > staticValue.getMaxHttpDownloadLength()) {
                throw new IllegalArgumentException("HTTP内容长度超过限制,实际大小为:" + contentLength + ",限制最大值为:" + staticValue.getMaxHttpDownloadLength());
            }
            byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
            if (htmlCharset != null) {
                return new String(contentBytes, htmlCharset);
            } else {
                LOG.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
                return new String(contentBytes);
            }
        } else {
            return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
        }
    }

    @Override
    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task) throws IOException {
        Page page;
        try {
            page = super.handleResponse(request, charset, httpResponse, task);
        } catch (IllegalArgumentException e) {
            writeExceptionLog(e, request);
            onError(request);
            LOG.warn("URL为:{} ,{}", request.getUrl(), e.getLocalizedMessage());
            throw e;
        }
        return page;
    }

    @Override
    public Page download(Request request, Task task) {
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }
        Set<Integer> acceptStatCode;
        String charset = null;
        Map<String, String> headers = null;
        if (site != null) {
            acceptStatCode = site.getAcceptStatCode();
            charset = site.getCharset();
            headers = site.getHeaders();
        } else {
            acceptStatCode = Sets.newHashSet(200);
        }
        LOG.trace("downloading page {}", request.getUrl());
        CloseableHttpResponse httpResponse = null;
        int statusCode = 0;
        HttpUriRequest httpUriRequest = null;
        try {
            httpUriRequest = getHttpUriRequest(request, site, headers);
            httpResponse = getHttpClient(site).execute(httpUriRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            request.putExtra(Request.STATUS_CODE, statusCode);
            if (statusAccept(acceptStatCode, statusCode)) {
                Page page = handleResponse(request, charset, httpResponse, task);
                onSuccess(request);
                return page;
            } else {
                LOG.warn("code error " + statusCode + "\t" + request.getUrl());
                return null;
            }
        } catch (IllegalArgumentException e) {
            if (httpUriRequest != null) {
                httpUriRequest.abort();
            }
            writeExceptionLog(e, request);
            onError(request);
            return null;
        } catch (IOException e) {
            LOG.warn("download page " + request.getUrl() + " error", e);
            if (site.getCycleRetryTimes() > 0) {
                return addToCycleRetry(request, site);
            }
            writeExceptionLog(e, request);
            onError(request);
            return null;
        } finally {
            request.putExtra(Request.STATUS_CODE, statusCode);
            try {
                if (httpResponse != null) {
                    //ensure the connection is released back to pool
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
                LOG.warn("close response fail", e);
            }
        }
    }

    private CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return httpClientGenerator.getClient(null);
        }
        String domain = site.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    private void writeExceptionLog(Exception e, Request request) {
        if (staticValue.isCommonsSpiderDebug()) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            request.putExtra("EXCEPTION", stringWriter.toString());
        } else {
            request.putExtra("EXCEPTION", e.getMessage());
        }
    }
}
