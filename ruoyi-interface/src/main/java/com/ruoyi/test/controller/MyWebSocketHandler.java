package com.ruoyi.test.controller;

import com.ruoyi.test.config.NettyConfig;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.CharsetUtil;

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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // handshake
        if(o instanceof FullHttpRequest) {
            handHttpRequest(channelHandlerContext, (FullHttpRequest) o);
        }else if (o instanceof WebSocketFrame) {
            handWebSocketFrame(channelHandlerContext, (WebSocketFrame) o);
        }
    }

    private void handHttpRequest(ChannelHandlerContext context, FullHttpRequest fullHttpRequest) {
        if(!fullHttpRequest.decoderResult().isSuccess() || !("websocket".equals(fullHttpRequest.headers().get("Upgrade")))) {
            sendHttpResponse(context, fullHttpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
    }

    private void sendHttpResponse(ChannelHandlerContext context, FullHttpRequest fullHttpRequest, DefaultFullHttpResponse defaultFullHttpResponse) {
        if (defaultFullHttpResponse.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(defaultFullHttpResponse.status().toString(), CharsetUtil.UTF_8);
            defaultFullHttpResponse.content().writeBytes(buf);
            buf.release();
        }

        ChannelFuture future = context.channel().writeAndFlush(defaultFullHttpResponse);
        if(defaultFullHttpResponse.status().code() != 200) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void handWebSocketFrame(ChannelHandlerContext context, WebSocketFrame webSocketFrame) {
        if(webSocketFrame instanceof CloseWebSocketFrame) {
            webSocketServerHandshaker.close(context.channel(), (CloseWebSocketFrame) webSocketFrame.retain());
        }
        if(webSocketFrame instanceof PingWebSocketFrame) {
            context.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
            return;
        }
        if(!(webSocketFrame instanceof TextWebSocketFrame)) {
            System.out.println("binary is not supported !");
            throw new RuntimeException(this.getClass().getName());
        }

        String request = ((TextWebSocketFrame) webSocketFrame).text();
        System.out.println("received:" + request);

        //response to clients
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(context.channel().id() + ":" + request);
        NettyConfig.group.writeAndFlush(textWebSocketFrame);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        NettyConfig.group.remove(context.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception {
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) throws Exception {
        throwable.printStackTrace();
        context.close();
    }




}
