package com.gs.spider.model.commons;

import org.jsoup.nodes.Document;

import java.util.Map;

/**
 * Page
 *
 * @author Gao Shen
 * @version 16/4/1
 */
public class Page {
    public transient Document document;
    public transient byte[] responseEntity;
    private String rawHtml;
    private Map<String, String> responseHeaders;
    private int statusCode;
    private String cookies;
    private transient byte[] capture;
    public Page() {
    }

    public Page(String html) {
        this.rawHtml = html;
    }

    public String getRawHtml() {
        return rawHtml;
    }

    public void setRawHtml(String rawHtml) {
        this.rawHtml = rawHtml;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(byte[] responseEntity) {
        this.responseEntity = responseEntity;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public byte[] getCapture() {
        return capture;
    }

    public void setCapture(byte[] capture) {
        this.capture = capture;
    }
}
