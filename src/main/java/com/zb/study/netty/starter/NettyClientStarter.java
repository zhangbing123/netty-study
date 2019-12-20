package com.zb.study.netty.starter;

import com.zb.study.netty.common.ChatMssage;
import com.zb.study.netty.common.MarshallingCodeCFactory;
import com.zb.study.netty.source.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;

import java.util.Scanner;

/**
 * @description: 客户端启动器
 * @author: zhangbing
 * @create: 2019-12-20 11:24
 **/
public class NettyClientStarter {

    private String host;
    private int port;
    private String clientName;

    private ChannelHandler handler = new NettyClientHandler();

    public NettyClientStarter(String host, int port, String clientName) {
        this.host = host;
        this.port = port;
        this.clientName = clientName;
    }

    public NettyClientStarter handler(ChannelHandler handler){
        this.handler = handler;
        return this;
    }


    public void start(){
        //客户端定义一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        //创建客户端启动对象Bootstrap,不是ServerBootstrap
        Bootstrap bootstrap = new Bootstrap();

        //配置相关参数
        bootstrap
                .group(group)//设置事件循环组
                .channel(NioSocketChannel.class)//使用NioSocketChannel作为客户端通道的实现
                .handler(new ChannelInitializer<SocketChannel>() {//配置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("decoder", MarshallingCodeCFactory.buildMarshallingDecoder());
                        pipeline.addLast("encoder", MarshallingCodeCFactory.buildMarshallingEncoder());
                        pipeline.addLast("http", new HttpClientCodec());
                        pipeline.addLast("chat", handler);//添加自定义处理器
                    }
                });

        try {
            //启动客户端去连接服务端
            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();
            System.out.println("localhost:"+channel.localAddress());
            Scanner scanner = new Scanner(System.in);
            ChatMssage chatMssage = null;
            while (scanner.hasNextLine()){
                String s = scanner.nextLine();
                if (s.equals("quit")){
                    System.exit(1);
                }
                chatMssage = new ChatMssage(clientName,s);
                channel.writeAndFlush(chatMssage);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

}
