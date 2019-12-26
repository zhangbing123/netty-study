package com.zb.study.netty.test;

import java.nio.ByteBuffer;

/**
 * @description: 直接内存与对外内存区别测试
 * @author: zhangbing
 * @create: 2019-12-26 13:54
 **/
public class MemoryTest {

    /**
     * 堆内存访问
     */
    public static void heapAccess() {
        long startTime = System.currentTimeMillis();
        //分配堆内存
        ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 200; j++) {
                byteBuffer.putInt(j);
            }
            byteBuffer.flip();
            for (int j = 0; j < 200; j++) {
                byteBuffer.getInt();
            }
            byteBuffer.clear();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("堆内存访问：" + (endTime - startTime));
    }

    /**
     * 直接内存访问
     */
    public static void directAccess() {
        long startTime = System.currentTimeMillis();
        //分配直接内存
        ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(1000);
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 200; j++) {
                directByteBuffer.putInt(j);
            }
            directByteBuffer.flip();
            for (int j = 0; j < 200; j++) {
                directByteBuffer.getInt();
            }
            directByteBuffer.clear();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("直接内存访问：" + (endTime - startTime));
    }

    /**
     * 堆内存申请
     */
    public static void heapAllocate() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ByteBuffer.allocate(100);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("堆内存申请：" + (endTime - startTime));
    }

    /**
     * 直接内存申请
     */
    public static void directAllocate() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ByteBuffer.allocateDirect(100);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("直接内存申请：" + (endTime - startTime));
    }

    /**
     * 以下测试结果得出：
     * 访问速度：
     *  直接内存块
     *  堆内存满
     * 内存申请速度：
     *  直接内存满
     *  堆内存块
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            heapAccess();
            directAccess();
        }

        for (int i = 0; i < 10; i++) {
            heapAllocate();
            directAllocate();
        }
    }

}
