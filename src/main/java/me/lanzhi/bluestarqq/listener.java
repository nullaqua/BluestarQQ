package me.lanzhi.bluestarqq;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.api.bot.MiraiFriend;
import me.dreamvoid.miraimc.api.bot.MiraiGroup;
import me.dreamvoid.miraimc.api.bot.group.MiraiNormalMember;
import me.dreamvoid.miraimc.bukkit.event.*;
import me.lanzhi.bluestarapi.Api.Bluestar;
import me.lanzhi.bluestarqq.events.QQChatEvent;
import me.lanzhi.bluestarqq.type.bind;
import me.lanzhi.bluestarqq.type.reply;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import static me.lanzhi.bluestarqq.BluestarQQ.config;
import static me.lanzhi.bluestarqq.BluestarQQ.plugin;

public class listener implements Listener
{
    List<String> pluginMessages=new ArrayList<>();

    @EventHandler
    public void onNewFriendRequest(MiraiNewFriendRequestEvent event)
    {
        System.out.println("有新好友申请,QQ号:"+event.getFromID()+",昵称:"+event.getFromNick());
        event.setAcceptRequest();
    }

    @EventHandler
    public void onNewFriend(MiraiFriendAddEvent event)
    {
        MiraiFriend friend=event.getFriend();
        System.out.println("新好友,QQ号:"+friend.getID()+",昵称:"+friend.getNick());
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
    public void onNewMemberJoin(MiraiGroupMemberJoinEvent event)
    {
        if (event.getGroupID()==config.getLong("maingroup"))
        {
            event.getGroup().sendMessage("欢迎"+event.getMember().getNick()+"加入蓝色之星bluestar");
            event.getGroup().sendMessage("服务器Ip,相关规定等信息,请查阅群公告");
            System.out.println("有新人加入主群,QQ号:"+event.getMember().getId()+"昵称:"+event.getMember().getNick());
        }
        else if (event.getGroupID()==config.getLong("chatgroup"))
        {
            event.getGroup().sendMessage("欢迎"+event.getMember().getNick()+"加入聊天群");
            event.getGroup().sendMessage("此群与服务器内消息互通哦");
            System.out.println("有新人加入聊天群,QQ号:"+event.getMember().getId()+"昵称:"+event.getMember().getNick());
        }
    }

    @EventHandler
    public void onFriendMessage(MiraiFriendMessageEvent event)
    {
        String message=event.getMessage();
        if ('/'==message.charAt(0)&&config.getList("op").contains(event.getSenderID()+""))
        {
            StringBuilder builder=new StringBuilder(message);
            builder.delete(0,1);
            Bluestar.useCommand(Bukkit.getConsoleSender(),builder.toString(),plugin);
        }
        else if (bind.get().getKey(event.getSenderID())!=null&&message.equals(bind.get().getKey(event.getSenderID())))
        {
            event.getFriend().sendMessage("您已成功绑定minecraft账号: "+Bukkit.getOfflinePlayer(bind.get().getUUID(event.getSenderID())).getName());
            System.out.println(bind.get().getUUID(event.getSenderID()).toString());
            MiraiMC.addBinding(bind.get().getUUID(event.getSenderID()).toString(),event.getSenderID());
            bind binds=bind.get();
            binds.removebind(event.getSenderID());
            binds.save();
        }
        else
        {
            decide(event);
        }
    }

    @EventHandler
    public void onGroupMessage(MiraiGroupMessageEvent event)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                decide(event);
            }
        }.runTaskAsynchronously(plugin);
        if (bind.get().getKey(event.getSenderID())!=null&&event.getMessage().equals(bind.get().getKey(event.getSenderID())))
        {
            event.getGroup().sendMessageMirai("[mirai:at:"+event.getSenderID()+"]您已成功绑定minecraft账号: "+Bukkit.getOfflinePlayer(bind.get().getUUID(event.getSenderID())).getName());
            System.out.println(bind.get().getUUID(event.getSenderID()).toString());
            MiraiMC.addBinding(bind.get().getUUID(event.getSenderID()).toString(),event.getSenderID());
            bind binds=bind.get();
            binds.removebind(event.getSenderID());
            binds.save();
        }
        else if (event.getGroupID()==config.getLong("chatgroup"))
        {
            String name=event.getSenderNameCard();
            String message=event.getMessage();
            MiraiBot bot=MiraiBot.getBot(event.getBotID());
            MiraiGroup group=bot.getGroup(event.getGroupID());
            MiraiNormalMember snder=group.getMember(event.getSenderID());
            if ("".equalsIgnoreCase(name)||name==null)
            {
                name=snder.getNick();
            }
            int cnt=0;
            while ((cnt=message.indexOf("@",cnt))!=-1)
            {
                int i=cnt+1;
                String qqid="";
                while ("0123456789".indexOf(message.charAt(i))!=-1)
                {
                    qqid=qqid+message.charAt(i);
                    i++;
                }
                if (!"".equalsIgnoreCase(qqid))
                {
                    long id=Long.parseLong(qqid);
                    String qqnick=group.getMember(id).getNick();
                    if (!"".equalsIgnoreCase(qqnick))
                    {
                        message=message.substring(0,cnt)+"@"+qqnick+" "+message.substring(i+1);
                    }
                }
                cnt++;
            }
            QQChatEvent qqChatEvent=new QQChatEvent(event,message);
            Bukkit.getPluginManager().callEvent(qqChatEvent);
            String message1=ChatColor.translateAlternateColorCodes('&',"&8[&3 QQ &8] &7"+name+": "+qqChatEvent.getMessage());
            pluginMessages.add(message1);
            Bukkit.getServer().broadcastMessage(message1);
        }
    }

    private static void decide(Event event)
    {
        String message;
        Object sender;
        OfflinePlayer player=null;
        long id;
        Long groupId=(long) 0;
        if (event instanceof MiraiGroupMessageEvent)
        {
            message=((MiraiGroupMessageEvent) event).getMessage();
            sender=((MiraiGroupMessageEvent) event).getGroup();
            id=((MiraiGroupMessageEvent) event).getSenderID();
            groupId=((MiraiGroupMessageEvent) event).getGroupID();
            String uuid=MiraiMC.getBinding(((MiraiGroupMessageEvent) event).getSenderID());
            if (!"".equalsIgnoreCase(uuid))
            {
                player=Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            }
        }
        else if (event instanceof MiraiFriendMessageEvent)
        {
            message=((MiraiFriendMessageEvent) event).getMessage();
            sender=((MiraiFriendMessageEvent) event).getFriend();
            id=((MiraiFriendMessageEvent) event).getFriend().getID();
            String uuid=MiraiMC.getBinding(((MiraiFriendMessageEvent) event).getSenderID());
            if (!"".equalsIgnoreCase(uuid))
            {
                player=Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            }
        }
        else
        {
            return;
        }
        message=message.toLowerCase();
        Map<String, Object> map=((reply) config.get("reply")).serialize();
        for (String key: map.keySet())
        {
            if (Pattern.compile(key).matcher(message).find())
            {
                HashMap<String, Object> m=(HashMap<String, Object>) map.get(key);
                String mode=m.get("mode").toString();
                if ((boolean) m.get("bind")&&player==null)
                {
                    if (sender instanceof MiraiGroup)
                    {
                        ((MiraiGroup) sender).sendMessageMirai("[mirai:at:"+id+"] \n错误!请绑定minecraft账号!");
                    }
                    else
                    {
                        ((MiraiFriend) sender).sendMessage("错误!请绑定minecraft账号!");
                    }
                    continue;
                }
                switch (mode)
                {
                    case "image":
                    {
                        File[] files=new File(plugin.getDataFolder(),m.get("file").toString()).listFiles();
                        if (sender instanceof MiraiGroup)
                        {
                            if (m.get("group") instanceof List&&m.get("group")!=null)
                            {
                                if (!((List<?>) m.get("group")).contains(groupId.toString()))
                                {
                                    break;
                                }
                            }
                            String file="[mirai:image:"+((MiraiGroup) sender).uploadImage(files[Math.abs(ThreadLocalRandom.current().nextInt())%files.length])+"]";
                            ((MiraiGroup) sender).sendMessageMirai("[mirai:at:"+id+"] \n"+ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player,m.get("message").toString()))+file);
                        }
                        else
                        {
                            if (m.get("friend") instanceof List&&m.get("friend")!=null)
                            {
                                if (!((List<?>) m.get("friend")).contains(id+""))
                                {
                                    break;
                                }
                            }
                            String file="[mirai:image:"+((MiraiFriend) sender).uploadImage(files[Math.abs(ThreadLocalRandom.current().nextInt())%files.length])+"]";
                            ((MiraiFriend) sender).sendMessageMirai(ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player,m.get("message").toString()))+file);
                        }
                        break;
                    }
                    case "message":
                    {
                        if (sender instanceof MiraiGroup)
                        {
                            if (m.get("group") instanceof List&&m.get("group")!=null)
                            {
                                if (!((List<?>) m.get("group")).contains(groupId.toString()))
                                {
                                    break;
                                }
                            }
                            ((MiraiGroup) sender).sendMessageMirai("[mirai:at:"+id+"] \n"+ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player,m.get("message").toString())));
                        }
                        else
                        {
                            if (m.get("friend") instanceof List&&m.get("friend")!=null)
                            {
                                if (!((List<?>) m.get("friend")).contains(id+""))
                                {
                                    break;
                                }
                            }
                            ((MiraiFriend) sender).sendMessageMirai(ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player,m.get("message").toString())));
                        }
                        break;
                    }
                    default:
                    {
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        MiraiBot bot=MiraiBot.getBot(config.getLong("bot"));
        bot.getGroup(config.getLong("chatgroup")).sendMessage(event.getPlayer().getName()+" 被吓跑了!");
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        MiraiBot bot=MiraiBot.getBot(config.getLong("bot"));
        String prefix=PlaceholderAPI.setPlaceholders(event.getPlayer(),"%vault_prefix%");
        String suffix=PlaceholderAPI.setPlaceholders(event.getPlayer(),"%vault_suffix%");
        bot.getGroup(config.getLong("chatgroup")).sendMessage(ChatColor.stripColor(suffix+"["+prefix+"]"+event.getPlayer().getName()+": "+event.getMessage()));
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                MiraiBot bot=MiraiBot.getBot(config.getLong("bot"));
                bot.getGroup(config.getLong("chatgroup")).sendMessage("☠️"+ChatColor.stripColor(event.getDeathMessage()));
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
        if (!event.isAsynchronous())
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    broadcastMessageToQQ(event.getMessage());
                }
            }.runTaskAsynchronously(plugin);
        }
        else
        {
            broadcastMessageToQQ(event.getMessage());
        }
    }

    private synchronized void broadcastMessageToQQ(String message)
    {
        if (pluginMessages.remove(message))
        {
            return;
        }
        MiraiBot bot=MiraiBot.getBot(config.getLong("bot"));
        try
        {
            bot.getGroup(config.getLong("chatgroup")).sendMessage(ChatColor.stripColor(message));
        }
        catch (Throwable e)
        {
            Bukkit.getLogger().warning("广播空消息");
        }
    }
}
