package com.ruoyi.test.util;

import java.net.SocketAddress;

import io.netty.channel.Channel;

public class NettyUtil {
    public static String parseChannelRemoteAddr(final Channel channel) {
        if(null == channel) {
            return "";
        }
        SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if(addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if(index > 0) {
                return addr.substring(index + 1);
            }
            return addr;
        }
        return "";
    }
}
