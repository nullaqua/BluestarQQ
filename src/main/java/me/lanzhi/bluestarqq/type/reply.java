package me.lanzhi.bluestarqq.type;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

public class reply implements ConfigurationSerializable
{
    final private Map<String, Object> map;

    public reply(Map<String, Object> map)
    {
        this.map=map;
    }

    @Override
    public Map<String, Object> serialize()
    {
        return map;
    }
}
