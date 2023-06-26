package com.meicilly.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建两个线程组bossGroup和workerGroup
         * 1.bossGroup只是处理连接请求 真正的和客户端业务处理会交给workerGroup完成
         * 2.bossGroup和workerGroup含有的子线程(NioEventLoop)的个数
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // TODO: 2023/6/24 创建服务端的启动对象 配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // TODO: 2023/6/24 使用链式编程进行设置
            bootstrap.group(bossGroup,workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户socketchannel hashcode=" + ch.hashCode()); //可以使用一个集合管理 SocketChannel， 再推送消息时，可以将业务加入到各个channel
                            ch.pipeline().addLast(new NettyServerHandler()); //给workerGroup的EventLoop对应的管道设置处理器
                        }
                    });
            System.out.println("...服务器器 is ready ...");
            //绑定一个端口并且同步 生成了一个ChannelFuture对象
            //启动服务器
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // TODO: 2023/6/26 给cf注册监听器 监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(cf.isDone()){
                        System.out.println("监听端口 6668 成功");
                    }else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
