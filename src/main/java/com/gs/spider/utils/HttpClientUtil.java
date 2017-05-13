package com.gs.spider.utils;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

@Component
@Scope("prototype")
public class HttpClientUtil {
    static HttpClient hc = new HttpClient();

    static {
        hc.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        hc.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
        hc.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        hc.getHttpConnectionManager().getParams().setSoTimeout(15000);
    }

    public String get(String url) throws IOException {
//        clearCookies();
        GetMethod g = new GetMethod(url);
        hc.executeMethod(g);
        return g.getResponseBodyAsString();
    }

    public byte[] getAsByte(String url) throws IOException {
//        clearCookies();
        GetMethod g = new GetMethod(url);
        hc.executeMethod(g);
        return g.getResponseBody();
    }

    public String getWithRealHeader(String url) throws IOException {
//        clearCookies();
        GetMethod g = new GetMethod(url);
        ////////////////////////
        g.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        g.addRequestHeader("Accept-Language", "zh-cn");
        g.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        g.addRequestHeader("Keep-Alive", "300");
        g.addRequestHeader("Connection", "Keep-Alive");
        g.addRequestHeader("Cache-Control", "no-cache");
        ///////////////////////
        hc.executeMethod(g);
        return g.getResponseBodyAsString();
    }


    public String get(String url, String cookies) throws
            IOException {
//        clearCookies();
        GetMethod g = new GetMethod(url);
        g.setFollowRedirects(false);
        if (StringUtils.isNotEmpty(cookies)) {
            g.addRequestHeader("cookie", cookies);
        }
        hc.executeMethod(g);
        return g.getResponseBodyAsString();
    }

    public String get(String url, Cookie[] cookies) throws
            IOException {
//        clearCookies();
        GetMethod g = new GetMethod(url);
        g.setFollowRedirects(false);
        hc.getState().addCookies(cookies);
        hc.executeMethod(g);
        return g.getResponseBodyAsString();
    }

    public String get(String url, String cookies, boolean followRedirects) throws
            IOException {
//        clearCookies();
        GetMethod g = new GetMethod(url);
        g.setFollowRedirects(followRedirects);
        if (StringUtils.isNotEmpty(cookies)) {
            g.addRequestHeader("cookie", cookies);
        }
        hc.executeMethod(g);
        return g.getResponseBodyAsString();
    }

    public String get(String url, boolean followRedirects) throws
            IOException {
//        clearCookies();
        GetMethod g = new GetMethod(url);
        g.setFollowRedirects(followRedirects);
        hc.executeMethod(g);
        return g.getResponseBodyAsString();
    }

    public String getHeader(String url, String cookies, String headername) throws IOException {
//        clearCookies();
        GetMethod g = new GetMethod(url);
        g.setFollowRedirects(false);
        if (StringUtils.isNotEmpty(cookies)) {
            g.addRequestHeader("cookie", cookies);
        }
        hc.executeMethod(g);
        return g.getResponseHeader(headername) == null ? null : g.getResponseHeader(headername).getValue();
    }

    public String post(String postURL, Map<String, String> partam, String cookies)
            throws IOException {
//        clearCookies();
        PostMethod p = new PostMethod(postURL);
        for (String key : partam.keySet()) {
            if (partam.get(key) != null) {
                p.setParameter(key, partam.get(key));
            }
        }
        if (StringUtils.isNotEmpty(cookies)) {
            p.addRequestHeader("cookie", cookies);
        }
        hc.executeMethod(p);
        return p.getResponseBodyAsString();
    }

    public String post(String url, String data) throws IOException {
        PostMethod post = new PostMethod(url);
        if (data != null && !data.isEmpty()) {
            post.addRequestHeader("Content-Type", "application/json");
            post.setRequestEntity(new StringRequestEntity(data, "application/json", "utf8"));
        }
        hc.executeMethod(post);
        return post.getResponseBodyAsString();
    }

    public String post(String postURL, Map<String, String> partam, String cookies, Map<String, String> header)
            throws IOException {
//        clearCookies();
        PostMethod p = new PostMethod(postURL);
        String reqEntity = "";
        for (Map.Entry<String, String> entry : partam.entrySet()) {
            reqEntity += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf8") + "&";
        }
//        p.setRequestBody(nameValuePair);
        p.setRequestEntity(new StringRequestEntity(reqEntity));
        if (StringUtils.isNotEmpty(cookies)) {
            p.addRequestHeader("cookie", cookies);
        }
        for (Map.Entry<String, String> entry : header.entrySet()) {
            p.addRequestHeader(entry.getKey(), entry.getValue());
        }
        hc.executeMethod(p);
        return p.getResponseBodyAsString();
    }

    public String getCookie() {
        Cookie[] cookies = hc.getState().getCookies();
        String tmpcookies = "";
        for (Cookie c : cookies) {
            tmpcookies += c.toString() + ";";
        }
        return tmpcookies;
    }

    public void clearCookies() {
        hc.getState().clearCookies();
    }

    public void addCookie(String cookie, String domain) {
        String[] data = cookie.split(";");
        for (String s : data) {
            String[] kvPair = s.split("=");
            if (kvPair.length == 2) {
                String name = kvPair[0];
                String value = kvPair[1];
                if (!name.equals("path") && !name.equals("domain")) {
                    hc.getState().addCookie(new Cookie(domain, name, value));
                }
            }
        }

    }

}
