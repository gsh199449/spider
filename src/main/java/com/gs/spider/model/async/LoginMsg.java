package com.gs.spider.model.async;

/**
 * LoginMsg
 *
 * @author Gao Shen
 * @version 16/4/22
 */
public class LoginMsg extends BaseMsg {

    public LoginMsg(String clientId) {
        super(clientId);
        this.setType(MsgType.LOGIN);
    }
}
