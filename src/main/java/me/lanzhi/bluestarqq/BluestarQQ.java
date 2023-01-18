package me.lanzhi.bluestarqq;

import me.lanzhi.bluestarbot.bluestarapi.config.YamlFile;
import me.lanzhi.bluestargame.bluestarapi.util.io.IOAccessor;
import me.lanzhi.bluestargame.bluestarapi.util.io.KeyObjectInputStream;
import me.lanzhi.bluestargame.bluestarapi.util.io.file.FileWithVersionReader;
import me.lanzhi.bluestargame.bluestarapi.util.io.file.FileWithVersionWriter;
import me.lanzhi.bluestargame.bluestarapi.util.io.file.ReadVersion;
import me.lanzhi.bluestarqq.commands.bindqq;
import me.lanzhi.bluestarqq.commands.maincommand;
import me.lanzhi.bluestarqq.message.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class BluestarQQ extends JavaPlugin
{
    public static BluestarQQ plugin;
    public static boolean debug=false;

    public static long mainGroup;
    public static long chatGroup;
    public static long bot;

    public static BindListener bindListener;
    private static CommandListener commandListener;

    public static void debug(String s)
    {
        if (debug)
        {
            System.out.println(s);
        }
    }

    @Override
    public void onEnable()
    {
        plugin=this;
        saveDefaultConfig();

        load();

        Bukkit.getPluginManager().registerEvents(new MainListener(),this);
        Objects.requireNonNull(getCommand("bluestarqq")).setExecutor(new maincommand());
        Objects.requireNonNull(getCommand("bindqq")).setExecutor(new bindqq());
        System.out.println("BluestarQQ已加载");
    }

    public void load()
    {
        var config=YamlFile.loadYamlFile(new File(getDataFolder(),"config.yml"));
        mainGroup=config.getLong("maingroup");
        chatGroup=config.getLong("chatgroup");
        bot=config.getLong("bot");

        var list=config.getLongList("op");
        new CommandListener(list).register(MessageListener.Priority.HIGH);
        new BroadcastListener().register(MessageListener.Priority.HIGH);

        var section=config.getConfigurationSection("reply");
        if (section!=null)
        {
            for (var x: AutoReply.parseAutoReplies(section))
            {
                x.register();
            }
        }

        FileWithVersionReader reader=new FileWithVersionReader(new File(getDataFolder(),"bind.db"));
        if (!reader.read(new FileWithVersionReader.Worker()
        {
            @ReadVersion("1.0.0")
            public void read(KeyObjectInputStream in) throws IOException, ClassNotFoundException
            {
                System.out.println("读取绑定信息version 1.0.0");
                bindListener=(BindListener) in.readObject();
            }

            @Override
            public void defaultRead(String version,KeyObjectInputStream stream)
            {
                System.out.println("读取绑定信息version "+version);
                bindListener=new BindListener();
            }
        },null,IOAccessor.hexKey()))
        {
            System.out.println("读取绑定信息失败");
            bindListener=new BindListener();
        }
        bindListener.register();
    }

    @Override
    public void onDisable()
    {
        save();
        System.out.println("BluestarQQ已卸载");
    }

    public void save()
    {
        var writer=new FileWithVersionWriter(new File(getDataFolder(),"bind.db"));
        try
        {
            writer.saveFile(null,IOAccessor.hexKey(),"1.0.0",out->out.writeObject(bindListener));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
