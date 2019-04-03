/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyunxing
 * @date: 2019-04-01 22:08
 * @des:
 */
@Component
public class WebSocketStart implements ApplicationRunner {
    @Value("${netty.port:8888}")
    private int port;
    @Value("${netty.readerIdleTimeSeconds:3}")
    private int readerIdleTimeSeconds;
    @Value("${netty.writerIdleTimeSeconds:5}")
    private int writerIdleTimeSeconds;
    @Value("${netty.allIdleTimeSeconds:0}")
    private int allIdleTimeSeconds;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new WebSocketChannelInitializer(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds));

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("netty start in " + port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
