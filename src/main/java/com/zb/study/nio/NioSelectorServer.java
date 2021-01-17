package com.zb.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @description: NIO模型服务端
 * @author: zhangbing
 * @create: 2019-12-16 15:14
 **/
public class NioSelectorServer {

    public static void main(String[] args) {
        try {
            //开启一个服务端的通道,并配置为非阻塞
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            //配置为非阻塞
            socketChannel.configureBlocking(false);
            //绑定通道到9002端口
            socketChannel.bind(new InetSocketAddress(9002));

            //开启一个选择器Selector
            Selector selector = Selector.open();
            //注册通道到选择器，并监听accept事件
            SelectionKey register = socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("注册的accept事件：selector："+selector+",SelectionKey："+register+",注册时间："+System.currentTimeMillis());

            //一直循环 进行处理客户端的连接和请求
            while(true){
                System.out.println("等待客户端触发事件...");
                //轮询监听key，select是阻塞的，accept()也是阻塞的 等待客户端的触发accept或者read事件触发
                int select = selector.select();
                System.out.println("有事件发生了...");

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    //删除本次处理的事件key
                    iterator.remove();
                    handle(key);
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handle(SelectionKey key) {
        if (key.isAcceptable()){//连接事件
            System.out.println("有客户端的连接事件发生了...");
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            try {
                //此处accept方法是阻塞的，但是这里因为是发生了连接事件，所以这个方法会马上执行
                SocketChannel socketChannel = channel.accept();
                //配置非阻塞
                socketChannel.configureBlocking(false);
                //监听read事件，一旦本次连接的客户端发数据过来 就会触发此事件
                SelectionKey register = socketChannel.register(key.selector(), SelectionKey.OP_READ);
                System.out.println("注册的read事件：selector："+key.selector()+",SelectionKey："+register+",注册时间："+System.currentTimeMillis());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (key.isReadable()){//获取客户端数据事件
            System.out.println("有客户端发送数据过来了...");
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();
            try {
                System.out.println("服务端读取数据：selector"+key.selector()+",key:"+key+",注册时间："+System.currentTimeMillis());
                int read = channel.read(buffer);
                if (read!=-1){
                    System.out.println("客户端数据读取完毕:"+new String(buffer.array()));
                }

                //写入返回给客户端的数据
                ByteBuffer bufferToWirte = ByteBuffer.wrap("hello,client".getBytes());
                channel.write(bufferToWirte);
                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);



            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
