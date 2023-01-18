package me.lanzhi.bluestarqq;

import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ReplyQQSender extends QQCommandSender
{
    private final MessageReceivedEvent event;

    public ReplyQQSender(MessageReceivedEvent event)
    {
        super(event.getContact());
        this.event=event;
    }

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
    @Override
    public void sendMessage(@NotNull String message)
    {
        message=ChatColor.translateAlternateColorCodes('&',message);
        message=ChatColor.stripColor(message);
        var finalMessage=message;
        new Thread(()->event.reply(finalMessage)).start();
    }

    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
    @Override
    public @NotNull String getName()
    {
        return event.getSender().toString();
    }
}
