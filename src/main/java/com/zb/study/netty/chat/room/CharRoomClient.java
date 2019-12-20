package com.zb.study.netty.chat.room;

import com.zb.study.netty.starter.NettyClientStarter;

/**
 * @description: 聊天室客户端
 * @author: zhangbing
 * @create: 2019-12-20 11:31
 **/
public class CharRoomClient {

    public static void main(String[] args){
//        String clientName = "张三";
//        String clientName = "李四";
        String clientName = "王五";
        NettyClientStarter starter = new NettyClientStarter("localhost", 9003, clientName);
        starter.handler(new ChatRoomClientHandler()).start();
    }
}
