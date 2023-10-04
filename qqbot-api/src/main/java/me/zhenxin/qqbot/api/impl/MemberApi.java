/*
 * qq-official-bot-sdk - QQ Official Bot SDK For Java
 * Copyright (C) 2021-2022 xiaoye-bot Project Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.zhenxin.qqbot.api.impl;

import com.alibaba.fastjson.JSONArray;
import me.zhenxin.qqbot.api.BaseApi;
import me.zhenxin.qqbot.entity.AccessInfo;
import me.zhenxin.qqbot.entity.Member;
import me.zhenxin.qqbot.exception.ApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成员相关接口
 *
 * @author 真心
 * @since 2021/12/11 13:11
 */
public class MemberApi extends BaseApi {
    public MemberApi(AccessInfo accessInfo) {
        super(accessInfo);
    }

    /**
     * 获取成员列表 (仅私域可用)
     *
     * @param guildId 频道ID
     * @param limit   获取数量
     */
    public List<Member> getMemberList(String guildId, Integer limit) throws ApiException {
        JSONArray array = get("/guilds/" + guildId + "/members?limit=" + limit, JSONArray.class);
        return array.toJavaList(Member.class);
    }

    /**
     * 获取成员列表 (仅私域可用)
     *
     * @param guildId 频道ID
     * @param after   频道ID (读此ID之后的数据)
     * @param limit   获取数量
     */
    public List<Member> getMemberList(String guildId, String after, Integer limit) throws ApiException {
        StringBuilder sb = new StringBuilder("/guilds/" + guildId + "/members");
        sb.append("?after=").append(after);
        sb.append("&limit=").append(limit);
        JSONArray array = get(sb.toString(), JSONArray.class);
        return array.toJavaList(Member.class);
    }

    /**
     * 获取身份组成员列表 (仅私域可用)
     *
     * @param guildId 频道ID
     * @param roleId  身份组ID
     * @param limit   获取数量
     */
    public List<Member> getMemberListByRole(String guildId, String roleId, Integer limit) throws ApiException {
        JSONArray array = get("/guilds/" + guildId + "/roles/" + roleId + "/members?limit=" + limit, JSONArray.class);
        return array.toJavaList(Member.class);
    }

    /**
     * 获取身份组成员列表 (仅私域可用)
     *
     * @param guildId    频道ID
     * @param roleId     身份组ID
     * @param startIndex 将上一次回包中next填入， 如果是第一次请求填 0，默认为 0
     * @param limit      获取数量
     */
    public List<Member> getMemberListByRole(String guildId, String roleId, String startIndex, Integer limit) throws ApiException {
        StringBuilder sb = new StringBuilder("/guilds/" + guildId + "/roles/" + roleId + "/members");
        sb.append("?start_index=").append(startIndex);
        sb.append("&limit=").append(limit);
        JSONArray array = get(sb.toString(), JSONArray.class);
        return array.toJavaList(Member.class);
    }

    /**
     * 获取成员信息
     *
     * @param guildId 频道ID
     * @param userId  用户ID
     */
    public Member getMemberInfo(String guildId, String userId) throws ApiException {
        return get("/guilds/" + guildId + "/members/" + userId, Member.class);
    }

    /**
     * 删除频道成员 (仅私域可用)
     *
     * @param guildId              频道ID
     * @param userId               用户ID
     * @param addBlacklist         删除成员的同时，将该用户添加到频道黑名单中
     * @param deleteHistoryMsgDays 删除成员的同时，撤回该成员的消息，可以指定撤回消息的时间范围
     */
    public void deleteMember(String guildId, String userId, Boolean addBlacklist, int deleteHistoryMsgDays) throws ApiException {
        Map<String, Object> data = new HashMap<>();
        data.put("add_blacklist", addBlacklist);
        data.put("delete_history_msg_days", deleteHistoryMsgDays);
        delete("/guilds/" + guildId + "/members/" + userId, data);
    }
}
