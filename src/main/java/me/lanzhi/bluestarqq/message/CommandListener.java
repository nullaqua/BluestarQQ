package me.lanzhi.bluestarqq.message;

import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import me.lanzhi.bluestargame.bluestarapi.Bluestar;
import me.lanzhi.bluestarqq.BluestarQQ;
import me.lanzhi.bluestarqq.ReplyQQSender;

import java.util.List;

public class CommandListener extends MessageListener
{
    private final List<Long> operators;

    public CommandListener(List<Long> operators)
    {
        this.operators=operators;
    }

    @Override
    protected boolean onEvent(MessageReceivedEvent event)
    {
        if (!operators.contains(event.getSender().getId()))
        {
            return false;
        }
        if (event.getMessageAsString().startsWith("/")||event.getMessageAsString().startsWith("#"))
        {
            Bluestar.getCommandManager()
                    .useCommand(new ReplyQQSender(event),event.getMessageAsString().substring(1),BluestarQQ.plugin);
            return true;
        }
        return false;
    }

    @Override
    protected String name()
    {
        return "CommandListener";
    }
}