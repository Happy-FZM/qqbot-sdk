package me.zhenxin.qqbot.core;

import java.util.TimerTask;

/**
 * 心跳计时器
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2021/12/9 11:34
 */
class HeartbeatTimer extends TimerTask {
    private final WSClient client;

    public HeartbeatTimer(WSClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        client.sendHeartbeat();
    }
}
