package com.meicilly.advance;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;

public class HelloServer {
    public static void main(String[] args) {
        // TODO: 2023-05-15 启动器 负责组装netty组件 启动服务器
        new ServerBootstrap()
                // TODO: 2023-05-15 BossEventLoop WorkerEventLoop group组
                .group(new NioEventLoopGroup())
                // TODO: 2023-05-15 选择服务的ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // TODO: 2023-05-15 boss处理连接  worker(child)负责处理读写 决定了worker（child）能执行哪些操作（handler）
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                // TODO: 2023-05-15 添加具体的handler
                                //System.out.println("进入了服务端");
                                ch.pipeline().addLast(new LoggingHandler());
                                ch.pipeline().addLast(new StringDecoder());//将ByteBuf转换成字符串
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    @Override // 读事件
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg); // 打印上一步转换好的字符串
                                    }
                                });

                            }
                        }
                )
                .bind(8080);//绑定端口
    }
}
