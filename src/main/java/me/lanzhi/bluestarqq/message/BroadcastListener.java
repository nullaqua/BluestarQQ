package me.lanzhi.bluestarqq.message;

import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import me.lanzhi.bluestarbot.api.event.message.received.GroupMessageEvent;
import me.lanzhi.bluestarbot.bluestarapi.player.chat.GradientColor;
import me.lanzhi.bluestarqq.BluestarQQ;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BroadcastListener extends MessageListener
{
    @Override
    protected boolean onEvent(MessageReceivedEvent event)
    {
        if (event instanceof GroupMessageEvent&&BluestarQQ.chatGroup==event.getContact().getId())
        {
            String h=String.format("&7来自QQ的消息:\n发送人: %s(%d)\n发送时间: %s\n点击回复此消息",
                                   event.getSender().getName(),
                                   event.getSender().getId(),
                                   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String msg="&8[&3 QQ &8] &7"+event.getSender().getName()+": "+event.getMessageAsString();
            msg=GradientColor.setColor(msg);
            h=GradientColor.setColor(h);

            ComponentBuilder builder=new ComponentBuilder();
            builder.append(msg)
                   .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(h)))
                   .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                         "[reply:"+System.currentTimeMillis()+":"+event.getMessageAsCode()+"]"));
            for (Player player: Bukkit.getOnlinePlayers())
            {
                player.spigot().sendMessage(builder.create());
            }
        }
        return false;
    }

    @Override
    protected String name()
    {
        return "BroadcastListener";
    }
}
