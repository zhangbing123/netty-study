package com.zb.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NioServer {

    public static void main(String[] args) throws IOException {

        List<SocketChannel> channelList = new ArrayList<>();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);//设置非阻塞
        serverSocketChannel.bind(new InetSocketAddress(9000));
        System.out.println("服务启动成功");

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();//因为配置了非阻塞  所以这一步不会阻塞
            if (socketChannel != null) {
                System.out.println("客户端连接成功");
                socketChannel.configureBlocking(false);
                channelList.add(socketChannel);
            }

            Iterator<SocketChannel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                SocketChannel channel = iterator.next();
                ByteBuffer buffer = ByteBuffer.allocate(128);
                int read = channel.read(buffer);
                if (read > 0) {
                    System.out.println("接收到客户端数据：" + String.valueOf(buffer));
                } else if (read == -1) {
                    iterator.remove();//移除channel
                    System.out.println("客户端连接断开");
                }

            }
        }

    }
}
