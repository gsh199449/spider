package com.gs.spider.model.commons;

import java.util.Map;

/**
 * LoginInfo
 *
 * @author Gao Shen
 * @version 16/4/1
 */
public class LoginInfo {

    private Map<String, String> initHeaders;
    private String usernameXPath;
    private String passwordXPath;
    private String clickXPath;
    private String username;
    private String password;
    private String loginUrl;
    private String user_agent;
    private int timeout = 5000;

    public Map<String, String> getInitHeaders() {
        return initHeaders;
    }

    public void setInitHeaders(Map<String, String> initHeaders) {
        this.initHeaders = initHeaders;
    }

    public String getUsernameXPath() {
        return usernameXPath;
    }

    public void setUsernameXPath(String usernameXPath) {
        this.usernameXPath = usernameXPath;
    }

    public String getPasswordXPath() {
        return passwordXPath;
    }

    public void setPasswordXPath(String passwordXPath) {
        this.passwordXPath = passwordXPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getClickXPath() {
        return clickXPath;
    }

    public void setClickXPath(String clickXPath) {
        this.clickXPath = clickXPath;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "initHeaders=" + initHeaders +
                ", usernameXPath='" + usernameXPath + '\'' +
                ", passwordXPath='" + passwordXPath + '\'' +
                ", clickXPath='" + clickXPath + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                ", user_agent='" + user_agent + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
