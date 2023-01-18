package me.lanzhi.bluestarqq.events;

import me.lanzhi.bluestarbot.api.Bot;
import me.lanzhi.bluestarbot.api.contact.group.Group;
import me.lanzhi.bluestarbot.api.contact.group.GroupMember;
import me.lanzhi.bluestarbot.api.event.message.received.GroupMessageEvent;
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
    final Group group;
    final Bot bot;
    final long botId;
    final long senderId;
    final GroupMember sender;
    final String senderName;
    final GroupMessageEvent event;

    public QQChatEvent(GroupMessageEvent event,String message)
    {
        super(true);
        this.event=event;
        this.message=message;
        group=event.getGroup();
        botId=event.getBot().getId();
        bot=event.getBot();
        senderId=event.getSender().getId();
        sender=event.getSender();
        senderName=event.getSenderName();
    }

    public long getBotId()
    {
        return botId;
    }

    public long getSenderId()
    {
        return senderId;
    }

    public Bot getBot()
    {
        return bot;
    }

    public GroupMessageEvent getEvent()
    {
        return event;
    }

    public Group getGroup()
    {
        return group;
    }

    public GroupMember getSender()
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
