package com.zb.study.netty.starter;

import com.zb.study.netty.common.MarshallingCodeCFactory;
import com.zb.study.netty.base.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: netty 服务端启动对象
 * @author: zhangbing
 * @create: 2019-12-20 11:19
 **/
public class NettyServerStarter {
    private int port;

    private ChannelHandler handler = new NettyServerHandler();

    public NettyServerStarter(int port) {
        this.port = port;
    }

    public NettyServerStarter handler(ChannelHandler handler) {
        this.handler = handler;
        return this;
    }


    public void start() {

        if (port <= 0) {
            throw new RuntimeException("No port specified");
        }

        //创建两个线程组 bossGroup和workGroup,线程的个数默认为cup核数的两倍

        //创建bossGroup 负责处理客户端的连接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //创建workGroup 负责处理客户端的数据请求
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //创建服务端的启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //配置参数（链式编程的方式）
        serverBootstrap
                .group(bossGroup, workGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class) //使用netty封装的NioServerSocketChannel作为服务器的通道实现
                //初始化客户端连接队列大小，服务端处理客户端的连接请求是在同一时间只能处理一个客户端的连接
                //当有一次有多个客户端的连接,服务端需要把不能处理的客户端连接放到队列里 等待处理
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {//创建通道初始化对象，设置初始化参数
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("decoder", MarshallingCodeCFactory.buildMarshallingDecoder());
                        socketChannel.pipeline().addLast("encoder", MarshallingCodeCFactory.buildMarshallingEncoder());
                        socketChannel.pipeline().addLast(handler);//对workerGroup的SocketChannel设置处理器
                    }
                });
        //至此服务可以启动
        System.out.println("Netty Server Start...");

        try {
            //绑定一个端口并且同步, 生成了一个ChannelFuture异步对象，通过isDone()等方法可以判断异步事件的执行情况
            //启动服务器(并绑定端口)，因为bind是异步操作，需要调用sync方法来等待异步操作执行完毕
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //对通道关闭进行监听，closeFuture是异步操作，监听通道关闭
            // 通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
