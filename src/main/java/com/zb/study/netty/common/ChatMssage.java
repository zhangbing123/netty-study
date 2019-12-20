package com.zb.study.netty.common;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @description: 聊天信息对象
 * @author: zhangbing
 * @create: 2019-12-20 15:35
 **/
public class ChatMssage implements Serializable {

    private String clientName;

    private String msg;

    public ChatMssage(String clientName) {
        this.clientName = clientName;
    }

    public ChatMssage(String clientName, String msg) {
        this.clientName = clientName;
        this.msg = msg;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
