package com.meicilly.nio;

import java.nio.ByteBuffer;

import static com.meicilly.nio.ByteBufferUtil.debugAll;

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
 */
public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // TODO: 2023/5/6 写数据
        buffer.put(new byte[]{'a','b','c','d'});
        // TODO: 2023/5/6 切换到读模式
        buffer.flip();
        // TODO: 2023/5/6 从头开始读
        // buffer.get(new byte[4]);
        debugAll(buffer);
        // TODO: 2023/5/6 position位置会更新为0
        // buffer.rewind();
        //System.out.println((char) buffer.get());
        // TODO: 2023/5/6 mark做一个标记 记录position位置 reset是将position重置到mark 的位置
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        debugAll(buffer);
        // TODO: 2023/5/6 加标记 索引2 位置
        buffer.mark();
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        // TODO: 2023/5/6 将position重置到索引 2
        buffer.reset();
        debugAll(buffer);

        // TODO: 2023/5/6   get(i) 不会改变读索引的位置
        System.out.println((char) buffer.get(3));
        debugAll(buffer);
    }
}
