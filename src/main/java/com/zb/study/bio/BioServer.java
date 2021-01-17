package com.zb.study.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: bio模型的服务端
 * @author: zhangbing
 * @create: 2019-12-16 14:38
 **/
public class BioServer {

    /**
     * BIO 有两处地方进行阻塞
     * 第一个阻塞的地方是serverSocket.accept(); 等待客户端的连接，如果没有客户端连接 则一直阻塞在这
     * 第二个阻塞的地方是socket.getInputStream().read(bytes)；读取客户端发来的数据，如果一个客户端连接后  一直不发数据  则一直阻塞在这  别的客户端也无法连接服务端
     *
     * 第二个阻塞的地方可以开启多线程进行处理 这样可以解决因为read阻塞别的客户端无法连接的情况，但是一旦连接的客户端过多则开启的线程就过多。
     *
     * @param args
     */

    public static void main(String[] args) {
        //创建一个ServerSocket
        try {
            ServerSocket serverSocket = new ServerSocket(9001);

            while (true){
                System.out.println("等待连接...");
                //此方法阻塞,没有客户端连接过来 就阻塞在这，直到客户端连接请求到达
                Socket socket = serverSocket.accept();
                System.out.println("有客户端的连接过来...");

                //单线程操作客户端请求的数据 这一步可以使用多线程处理 但也有缺点  如果有几万个客户端  就需要起几万个线程处理  这是不现实的事情
                handle(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handle(Socket socket){
        System.out.println("当前线程ID："+ Thread.currentThread().getName());

        //字节数组 用来保存客户端发来的数据
        byte[] bytes = new byte[1024];

        System.out.println("准备开始read...");

        try {
            //读取客户端发来的数据，没有数据可读 就阻塞
            int read = socket.getInputStream().read(bytes);

            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (read != -1){
                System.out.println("客户端数据读取完毕:"+new String(bytes));
            }

            //返回数据给客户端
            socket.getOutputStream().write("Hi,Client".getBytes());

            socket.getOutputStream().flush();


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
