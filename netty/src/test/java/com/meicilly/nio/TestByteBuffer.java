package com.meicilly.nio;



import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ByteBuffer常见的方法
 * 1.使用allocate方法为ByteBuffer分配空间 Bytebuffer buf = ByteBuffer.allocate(16);
 * 2.向buffer写数据
 *    调用channel的read方法 int readBytes = channel.read(buf);
 *    调用buffer自己的put方法 buf.put((byte)127);
 * 3.从buffer中读取数据
 *    调用channel的write方法 int writeBytes = channel.write(buf);
 *    调用 buffer 自己的 get 方法 byte b = buf.get();
 *    get 方法会让 position 读指针向后走，如果想重复读取数据
 *       可以调用 rewind 方法将 position 重新置为 0
 *       或者调用 get(int i) 方法获取索引 i 的内容，它不会移动读指针
 * 4.mark 和 reset
 *    mark 是在读取时，做一个标记，即使 position 改变，只要调用 reset 就能回到 mark 的位置
 *
 * ByteBuffer结构
 *   capacity
 *   position
 *   limit
 *   在写模式下 position是写入位置 limit等于容量
 *   flip动作发生后 position切换为读取位置 limit切换为读取限制
 *   compact 方法，是把未读完的部分向前压缩，然后切换至写模式
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) throws IOException {
        // TODO: 2023/5/6 1. 输入输出流， 2. RandomAccessFile
        FileChannel channel = new FileInputStream("F:\\大数据资料\\learning\\BigData-learning\\data\\data.txt").getChannel();
        // TODO: 2023/5/6 准备缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        while (true){
            // TODO: 2023/5/6 从channel读取数据 向buffer写入
            int len = channel.read(buffer);
            //System.out.print(len);
            if (len == -1){//没有内容
                break;
            }
            // TODO: 2023/5/6 打印buffer内容
            buffer.flip(); //切换到读模式
            // TODO: 2023/5/6 是否还有剩余未读数据
            while (buffer.hasRemaining()){
                byte b = buffer.get();
                //log.debug("实际字节 {}", (char) b);
                System.out.println((char) b);
            }
            // TODO: 2023/5/6 切换为写模式
            buffer.clear();
        }
    }
}
