package com.ruoyi.test.handler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ruoyi.test.entity.UserInfo;
import com.ruoyi.test.proto.ChatProto;
import com.ruoyi.test.util.BlankUtil;
import com.ruoyi.test.util.NettyUtil;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class UserInfoManager {

    private static ConcurrentMap<Channel, UserInfo> userInfos = new ConcurrentHashMap<>();
    private static AtomicInteger userCount = new AtomicInteger(0);
    
    public static void addChannel(Channel channel) {
        String remoteAddr = NettyUtil.parseChannelRemoteAddr(channel);
        
        if(!channel.isActive()) {
            
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAddr(remoteAddr);
        userInfo.setChannel(channel);
        userInfos.put(channel, userInfo);
    }

    public static boolean saveUser(Channel channel, String nick) {
        UserInfo userInfo = userInfos.get(channel);
        if(userInfo == null) {
            return false;
        }
        if(!channel.isActive()) {
            return false;
        }

        userCount.incrementAndGet();
        userInfo.setNick(nick);
        userInfo.setUserId();
        userInfo.setTime(System.currentTimeMillis());

        return true;
    }

    public static void removeChannel(Channel channel) {
        try {
            channel.close();
            UserInfo userInfo = userInfos.get(channel);
            if(userInfo != null) {
                UserInfo tmp = userInfos.remove(channel);
                if(tmp != null) {
                    userCount.decrementAndGet();                }
            }
        } finally {

        }
    }

    public static void broadcastMess(int uid, String nick, String message) {
        if(!BlankUtil.isBlank(message)) {
            try {
                Set<Channel> keySet = userInfos.keySet();
                for(Channel ch : keySet) {
                    UserInfo userInfo = userInfos.get(ch);
                    if(userInfo == null) continue;
                    ch.writeAndFlush(new TextWebSocketFrame(ChatProto.buildMessProto(uid, nick, message)));
                }
            } finally {

            }
        }
    }


}
