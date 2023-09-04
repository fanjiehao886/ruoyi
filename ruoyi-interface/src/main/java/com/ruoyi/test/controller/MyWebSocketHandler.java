package com.ruoyi.test.controller;

import com.ruoyi.test.config.NettyConfig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketServerHandshaker webSocketServerHandshaker;
    private static final String WEB_SOCKET_URL = "ws://localhost:8888/webSocket";

    //called when the channel is buit
    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        NettyConfig.group.add(context.channel());
        System.out.print("start the channel...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'channelRead0'");
    }
}
