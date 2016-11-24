package com.gs.spider.model.async;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Task implements Cloneable {
    private String taskId;
    private String name;
    //    private Map<Date, String> descriptions = new TreeMap<>((o1, o2) -> o1.after(o2) ? 1 : -1);
    private Map<Date, String> descriptions = new LinkedHashMap<>();
    private State state;
    private long time;
    private int count;
    private List<String> callbackURL = Lists.newArrayList();
    private String callbackPara;
    private long period;
    private TimeUnit timeUnit;
    private Map<Object, Object> extraInfo = Maps.newHashMap();

    public Task(String taskId, String name, long time) {
        this.taskId = taskId;
        this.name = name;
        this.state = State.INIT;
        this.time = time;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        setDescription(description, (Object) null);
    }

    /**
     * 使用{@link String#format(String, Object...)}格式化字符串 使用%s占位符
     *
     * @param description
     * @param para
     */
    public void setDescription(String description, Object... para) {
        final String decs = para != null ? String.format(description, para) : description;
        Date date = new Date();
        for (Date k : descriptions.keySet()) {
            if (Math.abs(k.getTime() - date.getTime()) < 2000) {
                descriptions.put(k, descriptions.get(k) + "<br/>" + decs);
                return;
            }
        }
        descriptions.put(date, decs);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getCallbackURL() {
        return callbackURL;
    }

    public Task setCallbackURL(List<String> callbackURL) {
        this.callbackURL = callbackURL;
        return this;
    }

    public void addCallbackURL(String callbackURL) {
        this.callbackURL.add(callbackURL);
    }

    public String getCallbackPara() {
        return callbackPara;
    }

    public void setCallbackPara(String callbackPara) {
        this.callbackPara = callbackPara;
    }


    public Map<Date, String> getDescriptions() {
        return descriptions;
    }

    public Task setDescriptions(Map<Date, String> descriptions) {
        this.descriptions = descriptions;
        return this;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void increaseCount() {
        this.count++;
    }

    public Map<Object, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<Object, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void addExtraInfo(Object key, Object value) {
        extraInfo.put(key, value);
    }

    public Object getExtraInfoByKey(Object key) {
        return extraInfo.get(key);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", descriptions=" + descriptions +
                ", state=" + state +
                ", time=" + time +
                ", count=" + count +
                ", callbackURL='" + callbackURL + '\'' +
                ", callbackPara='" + callbackPara + '\'' +
                ", period=" + period +
                ", timeUnit=" + timeUnit +
                ", extraInfo=" + extraInfo +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return new EqualsBuilder()
                .append(getTaskId(), task.getTaskId())
                .append(getName(), task.getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getTaskId())
                .append(getName())
                .toHashCode();
    }
}