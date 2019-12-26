package com.zb.study.netty.base;


import com.zb.study.netty.starter.NettyClientStarter;

/**
 * @description: netty 客户端
 * @author: zhangbing
 * @create: 2019-12-20 10:51
 **/
public class NettyClient {
    public static void main(String[] args) {

        new NettyClientStarter("localhost",9002,"张三").start();
    }
}
