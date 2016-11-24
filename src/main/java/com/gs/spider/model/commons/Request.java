package com.gs.spider.model.commons;


import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.function.Function;

/**
 * Request
 *
 * @author Gao Shen
 * @version 16/4/1
 */
public class Request {
    private String url;
    private String user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36";
    private HttpMethod httpMethod = HttpMethod.GET;
    private boolean followRedirect = true;
    private boolean ajax = false;
    private boolean needLogin = false;
    private LoginInfo loginInfo;
    private Function<LoginInfo, Map<String, String>> loginFunc;
    private Map<String, String> initHeaders;
    private Map<String, String> para;
    private int timeout = 5000;

    public Request(String url) {
        this.url = url;
    }

    public Request(String url, boolean ajax) {
        this.url = url;
        this.ajax = ajax;
    }

    public Request() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public Map<String, String> getInitHeaders() {
        return initHeaders;
    }

    public void setInitHeaders(Map<String, String> initHeaders) {
        this.initHeaders = initHeaders;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public Map<String, String> getPara() {
        return para;
    }

    public void setPara(Map<String, String> para) {
        this.para = para;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public Function<LoginInfo, Map<String, String>> getLoginFunc() {
        return loginFunc;
    }

    public void setLoginFunc(Function<LoginInfo, Map<String, String>> loginFunc) {
        this.loginFunc = loginFunc;
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", user_agent='" + user_agent + '\'' +
                ", httpMethod=" + httpMethod +
                ", followRedirect=" + followRedirect +
                ", ajax=" + ajax +
                ", needLogin=" + needLogin +
                ", loginInfo=" + loginInfo +
                ", loginFunc=" + loginFunc +
                ", initHeaders=" + initHeaders +
                ", para=" + para +
                ", timeout=" + timeout +
                '}';
    }
}
