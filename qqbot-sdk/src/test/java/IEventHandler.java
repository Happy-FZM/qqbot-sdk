import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.zhenxin.qqbot.api.ApiManager;
import me.zhenxin.qqbot.entity.DirectMessageSession;
import me.zhenxin.qqbot.entity.Message;
import me.zhenxin.qqbot.entity.MessageMarkdown;
import me.zhenxin.qqbot.entity.api.ApiPermission;
import me.zhenxin.qqbot.entity.api.ApiPermissionDemand;
import me.zhenxin.qqbot.entity.api.ApiPermissionDemandIdentify;
import me.zhenxin.qqbot.entity.forum.thread.Thread;
import me.zhenxin.qqbot.event.AtMessageEvent;
import me.zhenxin.qqbot.event.DirectMessageEvent;
import me.zhenxin.qqbot.event.UserMessageEvent;
import me.zhenxin.qqbot.exception.ApiException;
import me.zhenxin.qqbot.template.EmbedTemplate;
import me.zhenxin.qqbot.template.TextThumbnailTemplate;
import me.zhenxin.qqbot.websocket.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 事件执行器
 *
 * @author 真心
 * @since 2021/12/11 21:28
 */
@Slf4j
@AllArgsConstructor
class IEventHandler extends EventHandler {
    private final ApiManager api;

    @Override
    protected void onUserMessage(UserMessageEvent event) {
        val message = event.getMessage();
        message(message);
    }

    @Override
    protected void onAtMessage(AtMessageEvent event) {
        val message = event.getMessage();
        message(message);
    }

    @Override
    protected void onDirectMessage(DirectMessageEvent event) {
        val message = event.getMessage();
        log.debug("{}", message);
        api.getDirectMessageApi().sendMessage(
                message.getGuildId(),
                "测试",
                message.getId()
        );
    }

    private void message(Message message) {
        val guildId = message.getGuildId();
        val channelId = message.getChannelId();
        val content = message.getContent();
        val messageId = message.getId();
        val author = message.getAuthor();
        try {
            val args = content.split(" ");
            val command = args[0];
            switch (command) {
                case "threads":
                    List<Thread> threads = api.getForumApi().getThreadList("4348967");
                    log.info("{}", threads);
                    api.getMessageApi().sendMessage(channelId, Arrays.toString(threads.toArray()), messageId);
                    break;
                case "md":
                    MessageMarkdown md = new MessageMarkdown();
                    md.setContent("测试");
                    api.getMessageApi().sendMessage(channelId, md);
                    break;
                case "error":
                    api.getMessageApi().sendMessage(
                            channelId,
                            "https://www.qq.com",
                            messageId
                    );
                    break;
                case "apiList":
                    List<ApiPermission> permissions = api.getGuildApi().getApiPermissions(guildId);
                    for (ApiPermission permission : permissions) {
                        log.debug("{}", permission);
                        api.getMessageApi().sendMessage(
                                channelId,
                                permission.toString(),
                                messageId
                        );
                    }
                    break;
                case "apiLink":
                    ApiPermissionDemandIdentify identify = new ApiPermissionDemandIdentify();
                    identify.setPath("/guilds/{guild_id}/members/{user_id}");
                    identify.setMethod("GET");
                    ApiPermissionDemand demand = api.getGuildApi().createApiPermissionDemand(
                            guildId,
                            channelId,
                            identify,
                            "测试"
                    );
                    api.getMessageApi().sendMessage(
                            channelId,
                            demand.toString(),
                            messageId);
                    break;
                case "dm":
                    DirectMessageSession dms = api.getDirectMessageApi().createSession(
                            author.getId(),
                            guildId
                    );
                    api.getDirectMessageApi().sendMessage(
                            dms.getGuildId(),
                            "主动私信测试",
                            null
                    );
                    break;
                case "push":
                    api.getMessageApi().sendMessage(
                            channelId,
                            "测试",
                            null
                    );
                    break;
                case "members":
                    val members = api.getGuildApi().getGuildMembers(guildId, 1000);
                    for (val member : members) {
                        member.getUser().setAvatar("");
                    }
                    api.getMessageApi().sendMessage(
                            channelId,
                            members.toString(),
                            messageId
                    );
                    break;
                case "info":
                    api.getChannelPermissionsApi().addChannelPermissions(channelId, author.getId(), 1);
                    break;
                case "muteAll":
                    api.getMuteApi().muteAll(guildId, 300);
                    break;
                case "muteMe":
                    api.getMuteApi().mute(guildId, author.getId(), 300);
                    java.lang.Thread.sleep(6000);
                    api.getMuteApi().mute(guildId, author.getId(), 0);
                    break;
                case "dMsg":
                    val m = api.getMessageApi().sendMessage(channelId, "你好", messageId);
                    val id = m.getId();
                    api.getMessageApi().deleteMessage(channelId, id);
                    break;
                case "embed":
                    val fields = new ArrayList<String>();
                    fields.add("测试");
                    fields.add("测试2");
                    fields.add(String.valueOf(System.currentTimeMillis()));
                    val embed = EmbedTemplate.builder()
                            .title("测试")
                            .prompt("[测试]Embed")
                            .thumbnail("https://b.armoe.cn/assets/image/logo.png")
                            .fields(fields)
                            .build().toMessageEmbed();
                    api.getMessageApi().sendMessage(channelId, embed, messageId);
                    break;
                case "ping":
                    api.getMessageApi().sendMessage(channelId, "pong", messageId);
                    break;
                case "ark":
                    val ark = TextThumbnailTemplate.builder()
                            .build().toMessageArk();
                    api.getMessageApi().sendMessage(channelId, ark, messageId);
                    break;
            }
        } catch (ApiException e) {
            log.error("消息处理发生异常: {} {}({})", e.getCode(), e.getMessage(), e.getError());
            api.getMessageApi().sendMessage(channelId, "消息处理失败: " + e.getMessage(), messageId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
