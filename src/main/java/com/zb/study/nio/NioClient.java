package com.zb.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * redis 使用的机制 多路复用
 * @description: NIO模型客户端
 * @author: zhangbing
 * @create: 2019-12-16 15:14
 **/
public class NioClient {

    public static void main(String[] args) {
        try {
            //开启一个连接服务端的通道
            SocketChannel channel = SocketChannel.open();
            //设置通道为非阻塞的
            channel.configureBlocking(false);
            // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调
            //用channel.finishConnect();才能完成连接  这个方法执行完  服务端就接受到请求了
            channel.connect(new InetSocketAddress("localhost",9002));

            //开启一个选择器(多路复用器)
            Selector selector = Selector.open();
            //通道注册到选择器中 并设置监听连接事件
            SelectionKey register = channel.register(selector, SelectionKey.OP_CONNECT);
            System.out.println("注册的Connect事件：selector："+selector+",SelectionKey："+register+",注册时间："+System.currentTimeMillis());
            int i= 0;
            // 轮询访问selector
            while (true){
                // 选择一组可以进行I/O操作的事件，放在selector中,客户端的该方法不会阻塞，
                // 这里和服务端的方法不一样，查看api注释可以知道，当至少一个通道被选中时，
                // selector的wakeup方法被调用，方法返回，而对于客户端来说，通道一直是被
                int select = selector.select();
                //遍历
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handle(key);
                }
                i++;
                if (i>3){

                    break;
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handle(SelectionKey key) throws IOException {
        if (key.isConnectable()){
            SocketChannel channel =(SocketChannel) key.channel();
            if (channel.isConnectionPending()){
                channel.finishConnect();
            }
            channel.configureBlocking(false);
            //在这里可以给服务端发送信息哦
            ByteBuffer buffer = ByteBuffer.wrap("HelloServer".getBytes());
            channel.write(buffer);
            //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
            SelectionKey register = channel.register(key.selector(), SelectionKey.OP_READ);// 获得了可读的事件
            System.out.println("注册的Read事件：selector："+key.selector()+",SelectionKey："+register+",注册时间："+System.currentTimeMillis());

        }else if (key.isReadable()){
            read(key);
        }
    }

    /***
     * 处理读取服务端发来的信息的事件
     * @param key
     * @throws IOException
     */
    public static void read(SelectionKey key) throws IOException {
        //和服务端的read方法一样
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel =(SocketChannel) key.channel();
        System.out.println("客户端读取数据：selector"+key.selector()+",key:"+key+",注册时间："+System.currentTimeMillis());
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(512);
        int len = channel.read(buffer);
        if(len != -1){
            System.out.println("客户端收到信息："+ new String(buffer.array(),0, len));
        }

        ByteBuffer bufferWrite = ByteBuffer.wrap("我收到消息了".getBytes());
        channel.write(bufferWrite);


    }


}
