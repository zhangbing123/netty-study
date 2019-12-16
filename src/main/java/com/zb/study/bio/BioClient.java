package com.zb.study.bio;

import java.io.IOException;
import java.net.Socket;

/**
 * @description: bio模型的客户端
 * @author: zhangbing
 * @create: 2019-12-16 14:38
 **/
public class BioClient {

    public static void main(String[] args) {
        Socket socket = null;
        try {
             socket = new Socket("localhost", 9001);

            System.out.println("已经连接到服务端...");

            //向服务端发送数据
            socket.getOutputStream().write("hello, Server".getBytes());
            socket.getOutputStream().flush();
            System.out.println("向服务端发送数据...");

            byte[] bytes = new byte[1024];
            //接受服务端返回来的数据
            socket.getInputStream().read(bytes);

            System.out.println("接受到来自服务端返回的数据；"+new String(bytes));


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
