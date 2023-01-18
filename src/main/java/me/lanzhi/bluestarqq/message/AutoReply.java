package me.lanzhi.bluestarqq.message;

import me.lanzhi.bluestarbot.api.BluestarBot;
import me.lanzhi.bluestarbot.api.contact.Contact;
import me.lanzhi.bluestarbot.api.contact.User;
import me.lanzhi.bluestarbot.api.contact.group.Group;
import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import me.lanzhi.bluestarqq.BluestarQQ;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public abstract class AutoReply extends MessageListener
{
    private final boolean bind;
    private final List<Long> groups;
    private final boolean 群聊名单为黑名单;
    private final List<Long> users;
    private final boolean 用户名单为黑名单;
    private final Pattern pattern;

    protected AutoReply(ConfigurationSection section,String name)
    {
        this.bind=section.getBoolean("bind",false);
        this.groups=section.getLongList("groups");
        this.群聊名单为黑名单=section.getBoolean("blackGroup",false);
        this.users=section.getLongList("users");
        this.用户名单为黑名单=section.getBoolean("blackUser",false);
        this.pattern=Pattern.compile(name);
    }

    public static List<AutoReply> parseAutoReplies(ConfigurationSection section)
    {
        List<AutoReply> res=new ArrayList<>();
        var keys=section.getKeys(false);
        for (var key: keys)
        {
            var subSection=section.getConfigurationSection(key);
            if (subSection==null)
            {
                continue;
            }
            var mode=subSection.getString("mode","");
            res.add(mode.equals("image")?new ImageReply(subSection,key):new TextReply(subSection,key));
        }
        return res;
    }

    public boolean bind()
    {
        return bind;
    }

    public List<Long> groups()
    {
        return groups;
    }

    public boolean 群聊名单为黑名单()
    {
        return 群聊名单为黑名单;
    }

    public List<Long> users()
    {
        return users;
    }

    public boolean 用户名单为黑名单()
    {
        return 用户名单为黑名单;
    }

    public boolean onEvent(MessageReceivedEvent event)
    {
        User user=event.getSender();
        Contact contact=event.getContact();
        UUID uuid=BluestarBot.getBind(user.getId());
        OfflinePlayer player=uuid==null?null:Bukkit.getOfflinePlayer(uuid);

        if ((contact instanceof Group)&&groups.size()>0&&群聊名单为黑名单==groups.contains(contact.getId()))
        {
            BluestarQQ.debug("群聊不在白名单/在黑名单");
            return false;
        }
        if (users.size()>0&&用户名单为黑名单==users.contains(user.getId()))
        {
            BluestarQQ.debug("用户不在白名单/在黑名单");
            return false;
        }

        //匹配成功的次数
        int count=0;
        var Matcher=pattern.matcher(event.getMessageAsCode());
        while (Matcher.find())
        {
            count++;
            if (count>=10)
            {
                count=10;
                break;
            }
        }
        if (count>0&&bind&&player==null)
        {
            event.reply("你可能需要先绑定QQ,才能使用此功能");
            return false;
        }
        while (count-->0)
        {
            onEvent(event,player);
        }
        return false;
    }

    public abstract void onEvent(MessageReceivedEvent event,OfflinePlayer player);

    @Override
    protected String name()
    {
        return "关键字:"+pattern.pattern();
    }
}