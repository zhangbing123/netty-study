package com.zb.study.netty.chat.room;


import com.zb.study.netty.starter.NettyServerStarter;

/**
 * @description: 聊天室的服务端
 * @author: zhangbing
 * @create: 2019-12-20 11:15
 **/
public class ChatRoomServer {


    public static void main(String[] args){
        new NettyServerStarter(9003).handler(new ChatRoomServerHandler()).start();
    }

}
