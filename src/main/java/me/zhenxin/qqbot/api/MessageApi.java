package me.zhenxin.qqbot.api;

import cn.hutool.json.JSONUtil;
import me.zhenxin.qqbot.pojo.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息相关接口
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2021/12/8 16:15
 */
public class MessageApi extends BaseApi {
    public MessageApi(Boolean isSandBoxMode, String token) {
        super(isSandBoxMode, token);
    }

    public Message sendTextMessage(String channelId, String content, String messageId) {
        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        data.put("msg_id", messageId);
        String result = post("/channels/" + channelId + "/messages", data);
        return JSONUtil.toBean(result, Message.class);
    }
}
