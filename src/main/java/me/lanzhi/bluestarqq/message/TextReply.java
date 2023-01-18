package me.lanzhi.bluestarqq.message;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import me.lanzhi.bluestarbot.api.message.MessageChain;
import me.lanzhi.bluestarbot.api.message.Text;
import me.lanzhi.bluestargame.bluestarapi.BluestarManager;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TextReply extends AutoReply
{
    private final List<String> messages;

    public TextReply(ConfigurationSection section,String name)
    {
        super(section,name);
        if (section.get("messages") instanceof List)
        {
            messages=section.getStringList("messages");
        }
        else
        {
            messages=new ArrayList<>();
            messages.add(section.getString("message"));
        }
    }

    @Override
    protected String name()
    {
        return "纯文本"+super.name();
    }

    @Override
    public void onEvent(MessageReceivedEvent event,OfflinePlayer player)
    {
        MessageChain.Builder builder=MessageChain.builder();
        //随机选取一条消息
        if (messages.size()>0)
        {
            var message=PlaceholderAPI.setPlaceholders(player,messages.get(BluestarManager.randomInt(messages.size())));
            message=ChatColor.translateAlternateColorCodes('&',message);
            message=ChatColor.stripColor(message);
            builder.append(Text.create(message));
        }
        event.reply(builder.build());
    }
}
