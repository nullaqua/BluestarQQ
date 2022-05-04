package me.lanzhi.bluestarqq.commands;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

import static me.lanzhi.bluestarqq.BluestarQQ.config;

public class maincommand implements CommandExecutor, TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args)
    {
        if ("reload".equalsIgnoreCase(args[0])&&args.length==1)
        {
            config.reload();
            sender.sendMessage(ChatColor.GREEN+"BluestarQQ已重新加载");
        }
        if ("bind".equalsIgnoreCase(args[0]))
        {
            MiraiMC.addBinding(Bukkit.getPlayer(args[1]).getUniqueId().toString(),Long.parseLong(args[2]));
        }
        if (args.length!=2)
        {
            return false;
        }
        if ("send".equalsIgnoreCase(args[0]))
        {
            MiraiBot bot=MiraiBot.getBot(config.getLong("bot"));
            bot.getGroup(config.getLong("chatgroup")).sendMessageMirai("系统消息: "+args[1]);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender,Command command,String s,String[] args)
    {
        if (args.length==1)
        {
            List<String> tablist=new ArrayList<>();
            tablist.add("send");
            tablist.add("reload");
            return tablist;
        }
        return new ArrayList<>();
    }
}
