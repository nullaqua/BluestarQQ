package me.lanzhi.bluestarqq.message;

import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import me.lanzhi.bluestarqq.BluestarQQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BindListener extends MessageListener implements Serializable
{
    private final Map<Object,Object> map;
    private final Map<Long,String> key;

    public BindListener()
    {
        map=new HashMap<>();
        key=new HashMap<>();
    }


    @Override
    protected boolean onEvent(MessageReceivedEvent event)
    {
        String key=this.key.get(event.getSender().getId());
        if (key!=null&&event.getMessageAsString().equals(key))
        {
            this.key.remove(event.getSender().getId());
            map.remove(map.remove(event.getSender().getId()));
            event.reply("绑定成功");
            BluestarQQ.plugin.save();
            return true;
        }
        return false;
    }

    @Override
    protected String name()
    {
        return "BindListener";
    }

    public void addBind(long qq,UUID uuid,String key)
    {
        this.key.put(qq,key);
        map.put(qq,uuid);
        map.put(uuid,qq);
        BluestarQQ.plugin.save();
    }
}