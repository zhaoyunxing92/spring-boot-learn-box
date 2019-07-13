/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.netty.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhaoyunxing
 * @date: 2019-04-02 14:59
 * @des:
 */
@Slf4j
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("收到消息：" + msg.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"))));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        System.out.println("handlerAdded:" + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.warn("handlerRemoved: {}", ctx.channel().id().asLongText());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常发生:{}", ctx.channel().id().asLongText());
        ctx.close();
        cause.printStackTrace(System.err);
    }

    /**
     * 客户端事件监听
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 检测心跳
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            switch (idleStateEvent.state()){
                //全部超时 ALL_IDLE
                case ALL_IDLE:
                    log.info("state {}", idleStateEvent.state());
                    break;
                // 写超时 WRITER_IDLE
                case WRITER_IDLE:
                    break;
                // 读超时 READER_IDLE
                default:
                    log.info("");
            }
            log.info("state {}", idleStateEvent.state());

            log.info("IdleStateEvent ip:{} time:{}", ctx.channel().remoteAddress().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));

        }

    }
}
