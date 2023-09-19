package com.ruoyi.test.handler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ruoyi.test.entity.UserInfo;
import com.ruoyi.test.util.NettyUtil;

import io.netty.channel.Channel;

public class UserInfoManager {

    private static ConcurrentMap<Channel, UserInfo> userInfos = new ConcurrentHashMap<>();
    
    public static void addChannel(Channel channel) {
        String remoteAddr = NettyUtil.parseChannelRemoteAddr(channel);
        
        if(!channel.isActive()) {
            
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAddr(remoteAddr);
        userInfo.setChannel(channel);
        userInfos.put(channel, userInfo);
    }
}
