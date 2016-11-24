package com.gs.spider.model.async;

/**
 * InfoMsg
 *
 * @author Gao Shen
 * @version 16/4/22
 */
public class InfoMsg extends BaseMsg {
    private String info;

    public InfoMsg(String clientId) {
        super(clientId);
        this.setType(MsgType.INFO);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
