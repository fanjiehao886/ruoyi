package com.ruoyi.test.proto;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;

public class ChatProto {
    public static final int MESS_PROTO = 6 << 8 | 220;   //common message 110 11011100

    private int uri;
    private String body;
    private Map<String, Object> extend = new HashMap<>();

    public ChatProto(int head, String body) {
        this.uri = head;
        this.body = body;
    }

    public static String buildMessProto(int uid, String nick, String mess) {
        ChatProto chatProto = new ChatProto(MESS_PROTO, mess);
        chatProto.extend.put("uid", uid);
        chatProto.extend.put("nick", nick);
        return JSONObject.toJSONString(chatProto);
    }
}
