package com.gs.spider.model.async;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CallbackReplyMsg
 *
 * @author Gao Shen
 * @version 16/4/22
 */
public class CallbackReplyMsg extends InfoMsg {
    private Logger LOG = LogManager.getLogger(CallbackReplyMsg.class);

    public CallbackReplyMsg(String clientId) {
        super(clientId);
        this.setType(MsgType.CALLBACK_REPLY);
    }
}
