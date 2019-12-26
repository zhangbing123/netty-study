package com.zb.study.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


/**
 * @description: 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
 * @author: zhangbing
 * @create: 2019-12-20 10:40
 **/
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler {

    /**
     * 读取客户端发送的数据的方法
     * @param ctx 连接通道
     * @param msg 客户端发送的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到客户端"+Thread.currentThread().getName()+"的消息："+byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    /**
     * 读取客户端数据完毕之后的方法（可以用来回写数据给客户端）
     * @param ctx 连接通道
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端的数据读取完毕，可以对数据进行业务处理...");
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello Client", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 出现异常的处理方法 通常用来关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
