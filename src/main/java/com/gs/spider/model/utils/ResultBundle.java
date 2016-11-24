package com.gs.spider.model.utils;

/**
 * 结果集
 * Created by gaoshen on 16/1/5.
 */
public class ResultBundle<T> {
    /**
     * 请求的参数
     */
    protected String keyword;
    /**
     * 返回结果的数量
     */
    protected int count;
    /**
     * 本次调用耗时
     */
    protected long time;
    /**
     * 本次调用是否成功
     */
    protected boolean success;
    /**
     * 如调用出现错误,错误信息
     */
    protected String errorMsg;
    /**
     * 本次调用的追踪ID
     */
    protected String traceId;
    /**
     * 结果
     */
    private T result;

    public ResultBundle() {
    }

    public ResultBundle(T result, String keyword, long time) {
        this.result = result;
        this.keyword = keyword;
        this.time = time;
        this.count = 1;
        this.success = true;
    }

    public ResultBundle(String keyword, long time, boolean success, String errorMsg) {
        result = null;
        this.success = success;
        this.errorMsg = errorMsg;
        this.keyword = keyword;
        this.time = time;
        this.count = 0;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "ResultBundle{" +
                "result=" + result +
                ", keyword='" + keyword + '\'' +
                ", count=" + count +
                ", time=" + time +
                ", success=" + success +
                ", errorMsg='" + errorMsg + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
