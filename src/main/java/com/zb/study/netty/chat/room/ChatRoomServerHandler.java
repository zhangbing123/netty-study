package com.zb.study.netty.chat.room;

import com.zb.study.netty.common.ChatMssage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @description: 聊天室服务端处理器
 * @author: zhangbing
 * @create: 2019-12-20 11:29
 **/
@ChannelHandler.Sharable
public class ChatRoomServerHandler extends SimpleChannelInboundHandler<ChatMssage> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当有客户端连接时，会触发此方法 把客户端连接加入到channels队列中
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

        //获取客户端channel
        Channel inComing = ctx.channel();
        String msg =  "[欢迎: " + inComing.remoteAddress() + "] 进入聊天室！\n";

        ChatMssage chatMssage = new ChatMssage(null, msg);

        System.out.println("[欢迎: " + inComing.remoteAddress() + "] 进入聊天室！");
        for (Channel channel : channels) {
            if (inComing != channel) {
                channel.writeAndFlush(chatMssage);
            }
        }
        //加入队列
        channels.add(inComing);
    }

    /**
     * 当有客户端断开连接时，触发此方法，把客户端的channel移除队列
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel rmChannel = ctx.channel();
        System.out.println("[再见: " + rmChannel.remoteAddress() + "] 离开聊天室！");
        channels.remove(rmChannel);
        for (Channel channel : channels) {
            if (rmChannel != channel) {
                channel.writeAndFlush("[再见: " + rmChannel.remoteAddress() + "] 离开聊天室！\n");
            }
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatMssage chatMssage) throws Exception {
        Channel inComing = channelHandlerContext.channel();
        String clientName = chatMssage.getClientName();
        StringBuilder msg = new StringBuilder(chatMssage.getMsg());
        System.out.println(clientName+":"+msg);

        for (Channel channel : channels) {
            chatMssage = new ChatMssage(clientName);
            if (channel != inComing) {
                String s = "["+clientName+"]:" + msg + "\n";
                chatMssage.setMsg(s);
                System.out.println("发送给别人"+chatMssage.getMsg());
                channel.writeAndFlush(chatMssage);
            } else {
                String s = "[自己]:" + msg + "\n";
                chatMssage.setMsg(s);
                System.out.println("发送给自己"+chatMssage.getMsg());
                inComing.writeAndFlush(chatMssage);
            }
        }
    }
}
