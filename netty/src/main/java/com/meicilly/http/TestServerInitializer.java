package com.meicilly.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // TODO: 2023/6/26 向管道加入处理数据
        ChannelPipeline pipeline = ch.pipeline();
        // TODO: 2023/6/26 加入一个netty 提供的httpServerCodec codec => [coder - decoder]
        // TODO: 2023/6/26 HttpServerCodec 是netty 提供处理的http 编-解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //2. 增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());

        System.out.println("ok~~~~");
    }
}
