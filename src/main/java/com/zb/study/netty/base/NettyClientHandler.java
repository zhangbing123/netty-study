package com.zb.study.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @description: 客户端自定义的事件处理器
 * @author: zhangbing
 * @create: 2019-12-20 10:55
 **/
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 连接服务端成功后触发此方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与服务端成功建立连接...");
        System.out.println("开始发送消息...");
        //连接服务端成功 开始发送数据
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello Server", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 服务端有数据回写触发此方法 进行服务端的数据读取
     * @param ctx 连接通道
     * @param msg 服务端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取客户端回写的数据
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到服务端的消息："+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端的地址为："+ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }


    /**
     * 读取服务端的数据完毕之后触发此方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端数据读取完毕，可以对数据进行业务处理...");
    }

    /**
     * 发生异常触发此方法 一般用来关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
