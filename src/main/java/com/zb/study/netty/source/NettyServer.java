package com.zb.study.netty.source;


import com.zb.study.netty.starter.NettyServerStarter;

/**
 * @description: netty 的服务端
 * @author: zhangbing
 * @create: 2019-12-20 10:04
 **/
public class NettyServer {

    public static void main(String[] args) {
        //创建启动器
        new NettyServerStarter(9002).start();
    }
}
