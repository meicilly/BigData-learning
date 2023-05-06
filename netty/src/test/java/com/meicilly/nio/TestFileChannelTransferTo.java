package com.meicilly.nio;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * 不能直接打开 FileChannel，必须通过 FileInputStream、FileOutputStream
 * 或者 RandomAccessFile 来获取 FileChannel，它们都有 getChannel 方法
 *
 * 通过FileInputStream 获取的channel只能读
 * 通过FileOutputStream 获取channel 只能写
 * 通过 RandomAccessFile 是否能读写根据构造 RandomAccessFile 时的读写模式决定
 */
public class TestFileChannelTransferTo {
    public static void main(String[] args) throws IOException {
        FileChannel from = new FileInputStream("F:\\大数据资料\\learning\\BigData-learning\\netty\\src\\test\\data\\data.txt").getChannel();
        FileChannel to = new FileOutputStream("F:\\大数据资料\\learning\\BigData-learning\\netty\\src\\test\\data\\to.txt").getChannel();
        long size = from.size();
        for (long left = size; left > 0; ) {
            System.out.println("position:" + (size - left) + " left:" + left);
            left -= from.transferTo((size - left), left, to);
        }
    }
}
