package me.lanzhi.bluestarqq.message;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import me.lanzhi.bluestarbot.api.message.Image;
import me.lanzhi.bluestarbot.api.message.MessageChain;
import me.lanzhi.bluestarbot.api.message.Text;
import me.lanzhi.bluestargame.bluestarapi.BluestarManager;
import me.lanzhi.bluestarqq.BluestarQQ;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.lanzhi.bluestarqq.BluestarQQ.plugin;

public class ImageReply extends AutoReply
{
    private final List<File> files;
    private final List<String> messages;

    public ImageReply(ConfigurationSection section,String name)
    {
        super(section,name);
        if (section.get("image") instanceof List)
        {
            BluestarQQ.debug("ImageReply: "+name+" is a list");
            files=getFiles(section.getStringList("image"));
        }
        else
        {
            BluestarQQ.debug("ImageReply: "+name+" is a single file");
            files=getFiles(section.getString("image"));
        }
        if (section.get("message") instanceof List)
        {
            messages=section.getStringList("message");
        }
        else
        {
            messages=new ArrayList<>();
            messages.add(section.getString("message"));
        }
    }

    private static List<File> getFiles(List<String> names)
    {
        List<File> files=new ArrayList<>();
        for (var name: names)
        {
            var file=new File(plugin.getDataFolder(),name);
            if (file.exists())
            {
                getFiles(file,files);
            }
        }
        return files;
    }

    private static List<File> getFiles(String file)
    {
        List<File> files=new ArrayList<>();
        getFiles(new File(plugin.getDataFolder(),file),files);
        return files;
    }

    private static void getFiles(File file,List<File> files)
    {
        BluestarQQ.debug("getFiles: "+file.getAbsolutePath());
        if (file.isDirectory())
        {
            for (var f: Objects.requireNonNull(file.listFiles()))
            {
                getFiles(f,files);
            }
        }
        else if (file.getName().endsWith(".jpg")||file.getName().endsWith(".png"))
        {
            files.add(file);
        }
    }

    @Override
    protected String name()
    {
        return "图片"+super.name();
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
        //随机选取一张图片
        File file=files.size()>0?files.get(BluestarManager.randomInt(files.size())):null;
        if (file!=null)
        {
            builder.append(Image.create(file,event.getContact()));
        }
        event.reply(builder.build());
    }
}
