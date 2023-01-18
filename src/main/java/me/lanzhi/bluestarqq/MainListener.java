package me.lanzhi.bluestarqq;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lanzhi.bluestarbot.api.BluestarBot;
import me.lanzhi.bluestarbot.api.Bot;
import me.lanzhi.bluestarbot.api.contact.Friend;
import me.lanzhi.bluestarbot.api.contact.group.Group;
import me.lanzhi.bluestarbot.api.event.BluestarBotEvent;
import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import me.lanzhi.bluestarbot.api.event.friend.FriendAddEvent;
import me.lanzhi.bluestarbot.api.event.friend.NewFriendRequestEvent;
import me.lanzhi.bluestarbot.api.event.group.member.list.MemberJoinEvent;
import me.lanzhi.bluestarbot.api.event.message.NudgeEvent;
import me.lanzhi.bluestarbot.api.message.Message;
import me.lanzhi.bluestarbot.api.message.Reply;
import me.lanzhi.bluestarqq.message.MessageListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.lanzhi.bluestarqq.BluestarQQ.*;

public class MainListener implements Listener
{
    private static final Pattern pattern=Pattern.compile("\\[reply:([0-9]+):(.+)]:::");

    @EventHandler
    public void onNewFriendRequest(NewFriendRequestEvent event)
    {
        System.out.println("有新好友申请,QQ号:"+event.getUserId()+",昵称:"+event.getUserName());
        event.accept();
    }

    @EventHandler
    public void onNewFriend(FriendAddEvent event)
    {
        Friend friend=event.getFriend();
        System.out.println("新好友,QQ号:"+friend.getId()+",昵称:"+friend.getName());
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                friend.sendMessage("你好!我是 蓝色之星Bluestar 的机器人");
            }
        }.runTaskLaterAsynchronously(plugin,40);
    }

    @EventHandler
    public void onNewMemberJoin(MemberJoinEvent event)
    {
        if (event.getGroup().getId()==mainGroup)
        {
            event.getGroup().sendMessage("欢迎"+event.getMember().getName()+"加入蓝色之星bluestar");
            event.getGroup().sendMessage("服务器Ip,相关规定等信息,请查阅群公告");
            System.out.println("有新人加入主群,QQ号:"+event.getMember().getId()+"昵称:"+event.getMember().getName());
        }
        else if (event.getGroup().getId()==chatGroup)
        {
            event.getGroup().sendMessage("欢迎"+event.getMember().getName()+"加入聊天群");
            event.getGroup().sendMessage("此群与服务器内消息互通哦");
            System.out.println("有新人加入聊天群,QQ号:"+event.getMember().getId()+"昵称:"+event.getMember().getName());
        }
    }

    @EventHandler
    public void onNudge(NudgeEvent e)
    {
        if (e.getTo().equals(e.getBot()))
        {
            e.getContact().nudge(e.getFrom());
        }
    }

    @EventHandler
    public void onQQMessage(BluestarBotEvent e)
    {
        if (!(e instanceof MessageReceivedEvent event))
        {
            return;
        }
        if (event.getBot().getId()==event.getSender().getId())
        {
            return;
        }
        MessageListener.event(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Bot bot=BluestarBot.getBot(BluestarQQ.bot);
        bot.getGroup(chatGroup).sendMessage(event.getPlayer().getName()+" 被吓跑了!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Bot bot=BluestarBot.getBot(BluestarQQ.bot);
        bot.getGroup(chatGroup).sendMessage(event.getPlayer().getName()+" 加入了游戏!");
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        Bot bot=BluestarBot.getBot(BluestarQQ.bot);
        Group chatGroup=bot.getGroup(BluestarQQ.chatGroup);

        try
        {
            Matcher matcher=pattern.matcher(event.getMessage());
            if (matcher.find())
            {
                long time=Long.parseLong(matcher.group(1));
                String message=matcher.group(2);
                String res=event.getMessage().substring(matcher.end());
                chatGroup.sendMessage(Message.getFromMiraiCode(res),
                                      new Reply(bot,new Date(time),Message.getFromMiraiCode(message)));
                return;
            }
        }
        catch (Exception ignored)
        {
        }

        String prefix=ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',
                                                                                  PlaceholderAPI.setPlaceholders(event.getPlayer(),
                                                                                                                 "%vault_prefix%")));
        String suffix=ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',
                                                                                  PlaceholderAPI.setPlaceholders(event.getPlayer(),
                                                                                                                 "%vault_suffix%")));
        chatGroup.sendMessage(ChatColor.stripColor(suffix+
                                                   "["+
                                                   prefix+
                                                   "]"+
                                                   event.getPlayer().getName()+
                                                   ": "+
                                                   event.getMessage()));
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bot bot=BluestarBot.getBot(BluestarQQ.bot);
                bot.getGroup(chatGroup).sendMessage("☠️"+ChatColor.stripColor(event.getDeathMessage()));
            }
        }.runTaskAsynchronously(plugin);
    }

    @EventHandler
    public synchronized void onBroadcastMessage(BroadcastMessageEvent event)
    {
        if (event.getMessage().isEmpty())
        {
            return;
        }
        new Thread(()->broadcastMessageToQQ(event.getMessage()));
    }

    private synchronized void broadcastMessageToQQ(String message)
    {
        Bot bot=BluestarBot.getBot(BluestarQQ.bot);
        try
        {
            bot.getGroup(chatGroup).sendMessage(ChatColor.stripColor(message));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
