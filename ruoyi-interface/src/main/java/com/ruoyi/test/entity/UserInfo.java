package com.ruoyi.test.entity;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.Channel;

public class UserInfo {
    private static AtomicInteger uidGener = new AtomicInteger(1000);

    private long time = 0;
    private int userId;
    private String nick;
    private Channel channel;
    private String addr;

    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }

    public String getNick() {
        return this.nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getUserId() {
        return this.userId;
    }
    public void setUserId() {
        this.userId = uidGener.incrementAndGet();
    }


    public Channel getChannel() {
        return this.channel;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getAddr() {
        return this.addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
}
