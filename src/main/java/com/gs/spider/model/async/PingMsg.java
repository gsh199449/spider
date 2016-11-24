package com.gs.spider.model.async;

/**
 * PingMsg
 *
 * @author Gao Shen
 * @version 16/4/22
 */
public class PingMsg extends InfoMsg {
    public PingMsg(String clientId) {
        super(clientId);
        this.setType(MsgType.PING);
    }
}
