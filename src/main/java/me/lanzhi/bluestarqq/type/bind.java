package me.lanzhi.bluestarqq.type;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.lanzhi.bluestarqq.BluestarQQ.data;

public class bind implements ConfigurationSerializable
{
    private Map<String, Object> map=new HashMap<>();

    public bind(Map<String, Object> map)
    {
        this.map=map;
    }

    public bind()
    {
    }

    public void addbind(UUID uuid,long id,long key)
    {
        Map<String, Map<String, String>> qq=(Map<String, Map<String, String>>) map.get("qq");
        Map<String, String> minecraft=(Map<String, String>) map.get("minecraft");
        if (minecraft==null)
        {
            minecraft=new HashMap<>();
        }
        if (qq==null)
        {
            qq=new HashMap<>();
        }
        Map<String, String> map1=new HashMap<>();
        map1.put("uuid",uuid.toString());
        map1.put("key",key+"");
        if (qq.get(id+"")!=null)
        {
            minecraft.remove(qq.get(id+"").get("uuid"));
            qq.remove(id+"");
        }
        if (minecraft.get(uuid.toString())!=null)
        {
            qq.remove(minecraft.get(uuid.toString()));
            minecraft.remove(uuid.toString());
        }
        qq.put(id+"",map1);
        minecraft.put(uuid.toString(),id+"");
        map.put("qq",qq);
        map.put("minecraft",minecraft);
    }

    public void removebind(long id)
    {
        Map<String, Map<String, String>> qq=(Map<String, Map<String, String>>) map.get("qq");
        Map<String, String> minecraft=(Map<String, String>) map.get("minecraft");
        minecraft.remove(qq.get(id+"").get("uuid"));
        qq.remove(id+"");
        map.put("qq",qq);
        map.put("minecraft",minecraft);
    }

    @Override
    public @NotNull Map<String, Object> serialize()
    {
        return map;
    }

    public void save()
    {
        data.set("bind",this);
    }

    public UUID getUUID(long id)
    {
        Map<String, Map<String, String>> qq=(Map<String, Map<String, String>>) map.get("qq");
        if (qq==null)
        {
            return null;
        }
        if (qq.get(id+"")==null)
        {
            return null;
        }
        return UUID.fromString(qq.get(id+"").get("uuid"));
    }

    public long getQQId(UUID id)
    {
        Map<String, String> minecraft=(Map<String, String>) map.get("minecraft");
        if (minecraft==null)
        {
            return 0;
        }
        return Long.parseLong(minecraft.get(id.toString()));
    }

    public String getKey(UUID id)
    {
        Map<String, String> minecraft=(Map<String, String>) map.get("minecraft");
        if (minecraft==null)
        {
            return null;
        }
        if (minecraft.get(id.toString())==null)
        {
            return null;
        }
        long qqid=Long.parseLong(minecraft.get(id.toString()));
        Map<String, Map<String, String>> qq=(Map<String, Map<String, String>>) map.get("qq");
        if (qq==null)
        {
            return null;
        }
        if (qq.get(qqid+"")==null)
        {
            return null;
        }
        return qq.get(qqid+"").get("key");
    }

    public String getKey(long qqid)
    {
        Map<String, Map<String, String>> qq=(Map<String, Map<String, String>>) map.get("qq");
        if (qq==null)
        {
            return null;
        }
        if (qq.get(qqid+"")==null)
        {
            return null;
        }
        return qq.get(qqid+"").get("key");
    }

    public static bind get()
    {
        return (bind) (data.get("bind")==null?new bind():data.get("bind"));
    }
}
