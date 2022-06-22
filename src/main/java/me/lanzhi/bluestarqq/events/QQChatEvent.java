package me.lanzhi.bluestarqq.events;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.bot.MiraiGroup;
import me.dreamvoid.miraimc.api.bot.group.MiraiNormalMember;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 一个被广播到Minecraft服务器的QQ群聊消息
 */
public class QQChatEvent extends Event
{
    private static final HandlerList handlers=new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    boolean cancelled=false;
    String message;
    final MiraiGroup group;
    final MiraiBot bot;
    final long botId;
    final long senderId;
    final MiraiNormalMember sender;
    final String senderName;
    final MiraiGroupMessageEvent event;

    public QQChatEvent(MiraiGroupMessageEvent event,String message)
    {
        super(true);
        this.event=event;
        this.message=message;
        group=event.getGroup();
        botId=event.getBotID();
        bot=MiraiBot.getBot(botId);
        senderId=event.getSenderID();
        sender=group.getMember(senderId);
        senderName=event.getSenderNameCard();
        if (senderName==null||senderName.equals(""))
        {
            sender.getNick();
        }
    }

    public long getBotId()
    {
        return botId;
    }

    public long getSenderId()
    {
        return senderId;
    }

    public MiraiBot getBot()
    {
        return bot;
    }

    public MiraiGroupMessageEvent getEvent()
    {
        return event;
    }

    public MiraiGroup getGroup()
    {
        return group;
    }

    public MiraiNormalMember getSender()
    {
        return sender;
    }

    public String getMessage()
    {
        return message;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public boolean isCancelled()
    {
        return cancelled;
    }

    public void setCancelled(boolean cancelled)
    {
        this.cancelled=cancelled;
    }

    public void setMessage(String message)
    {
        this.message=message;
    }
}
