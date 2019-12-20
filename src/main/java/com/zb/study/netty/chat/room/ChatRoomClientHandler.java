package com.zb.study.netty.chat.room;

import com.zb.study.netty.common.ChatMssage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 聊天室客户端处理器
 * @author: zhangbing
 * @create: 2019-12-20 11:32
 **/
public class ChatRoomClientHandler extends SimpleChannelInboundHandler<ChatMssage> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与服务端建立连接成功....");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatMssage chatMssage) throws Exception {
        System.out.println(chatMssage.getMsg());
    }
}
