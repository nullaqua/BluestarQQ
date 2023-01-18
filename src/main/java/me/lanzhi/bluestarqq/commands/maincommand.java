package me.lanzhi.bluestarqq.commands;

import me.lanzhi.bluestarbot.api.BluestarBot;
import me.lanzhi.bluestarbot.api.Bot;
import me.lanzhi.bluestarbot.api.message.Message;
import me.lanzhi.bluestarqq.BluestarQQ;
import me.lanzhi.bluestarqq.message.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.lanzhi.bluestarqq.BluestarQQ.*;

public class maincommand implements CommandExecutor, TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args)
    {
        if ("debug".equalsIgnoreCase(args[0]))
        {
            debug=!debug;
        }
        if ("reload".equalsIgnoreCase(args[0])&&args.length==1)
        {
            MessageListener.clearAll();
            plugin.load();
            sender.sendMessage(ChatColor.GREEN+"BluestarQQ已重新加载");
        }
        if ("bind".equalsIgnoreCase(args[0]))
        {
            BluestarBot.addBind(Bukkit.getPlayer(args[1]).getUniqueId(),Long.parseLong(args[2]));
        }
        if (args.length!=2)
        {
            return false;
        }
        if ("send".equalsIgnoreCase(args[0]))
        {
            Bot bot=BluestarBot.getBot(BluestarQQ.bot);
            bot.getGroup(chatGroup).sendMessage(Message.getFromMiraiCode("系统消息: "+args[1]));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender,Command command,String s,String[] args)
    {
        if (args.length==1)
        {
            return Arrays.asList("debug","reload","bind","send");
        }
        return new ArrayList<>();
    }
}
