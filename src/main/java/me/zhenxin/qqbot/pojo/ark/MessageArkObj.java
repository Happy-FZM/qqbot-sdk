package me.zhenxin.qqbot.pojo.ark;

import lombok.Data;

import java.util.List;

/**
 * ArkObj对象
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2021/12/8 16:35
 */
@Data
public class MessageArkObj {
    private List<MessageArkObjKv> objKv;
}
