package me.lanzhi.bluestarqq.message;

import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import me.lanzhi.bluestarqq.BluestarQQ;

import java.util.ArrayList;
import java.util.List;

public abstract class MessageListener
{
    protected MessageListener()
    {
    }

    public static void clearAll()
    {
        for (Priority priority: Priority.values())
        {
            priority.clear();
        }
    }

    public static void event(MessageReceivedEvent event)
    {
        for (var listener: Priority.HIGH.listeners)
        {
            BluestarQQ.debug("处理事件: "+listener.name()+" 优先级: "+Priority.HIGH.name());
            if (listener.onEvent(event))
            {
                return;
            }
        }
        for (var listener: Priority.NORMAL.listeners)
        {
            BluestarQQ.debug("处理事件: "+listener.name()+" 优先级: "+Priority.NORMAL.name());
            if (listener.onEvent(event))
            {
                return;
            }
        }
        for (var listener: Priority.LOW.listeners)
        {
            BluestarQQ.debug("处理事件: "+listener.name()+" 优先级: "+Priority.LOW.name());
            if (listener.onEvent(event))
            {
                return;
            }
        }
    }

    protected abstract String name();

    protected abstract boolean onEvent(MessageReceivedEvent event);

    public void register()
    {
        register(Priority.NORMAL);
    }

    public void register(Priority priority)
    {
        if (priority==null)
        {
            register();
            return;
        }
        priority.addListener(this);
    }

    public void unregister()
    {
        unregister(Priority.NORMAL);
    }

    public void unregister(Priority priority)
    {
        if (priority==null)
        {
            unregister();
            return;
        }
        priority.removeListener(this);
    }

    public void unregisterAll()
    {
        for (Priority priority: Priority.values())
        {
            unregister(priority);
        }
    }

    public static enum Priority
    {
        LOW,
        NORMAL,
        HIGH;
        private final List<MessageListener> listeners=new ArrayList<>();

        private void addListener(MessageListener listener)
        {
            BluestarQQ.debug("Registering listener "+listener.name()+" to priority "+this.name());
            listeners.add(listener);
        }

        private void removeListener(MessageListener listener)
        {
            BluestarQQ.debug("Unregistering listener "+listener.name()+" from priority "+this.name());
            listeners.remove(listener);
        }

        public void clear()
        {
            BluestarQQ.debug("Clearing listeners of priority "+this.name());
            listeners.clear();
        }
    }
}
