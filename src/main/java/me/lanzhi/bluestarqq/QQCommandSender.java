package me.lanzhi.bluestarqq;

import me.lanzhi.bluestarbot.api.contact.Contact;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class QQCommandSender implements ConsoleCommandSender
{
    private final Contact contact;

    public QQCommandSender(Contact contact)
    {
        this.contact=contact;
    }

    /**
     * Sends this sender a message
     *
     * @param sender  The sender of this message
     * @param message Message to be displayed
     */
    @Override
    public void sendMessage(@Nullable UUID sender,@NotNull String message)
    {
        sendMessage(message);
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
        new Thread(()->contact.sendMessage(finalMessage)).start();
    }

    /**
     * Sends this sender multiple messages
     *
     * @param sender   The sender of this message
     * @param messages An array of messages to be displayed
     */
    @Override
    public void sendMessage(@Nullable UUID sender,@NotNull String... messages)
    {
        sendMessage(messages);
    }

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     */
    @Override
    public void sendMessage(@NotNull String... messages)
    {
        for (String message: messages)
        {
            sendMessage(message);
        }
    }

    /**
     * Returns the server instance that this command is running on
     *
     * @return Server instance
     */
    @NotNull
    @Override
    public Server getServer()
    {
        return Bukkit.getConsoleSender().getServer();
    }

    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
    @NotNull
    @Override
    public String getName()
    {
        return contact.toString();
    }

    @NotNull
    @Override
    public Spigot spigot()
    {
        return new Spigot()
        {
            @Override
            public void sendMessage(@Nullable UUID sender,@NotNull BaseComponent component)
            {
                sendMessage(component);
            }

            @Override
            public void sendMessage(@NotNull BaseComponent component)
            {
                QQCommandSender.this.sendMessage(component.toPlainText());
            }

            @Override
            public void sendMessage(@Nullable UUID sender,@NotNull BaseComponent... components)
            {
                sendMessage(components);
            }

            @Override
            public void sendMessage(@NotNull BaseComponent... components)
            {
                for (BaseComponent component: components)
                {
                    sendMessage(component);
                }
            }
        };
    }

    /**
     * Tests to see of a Conversable object is actively engaged in a
     * conversation.
     *
     * @return True if a conversation is in progress
     */
    @Override
    public boolean isConversing()
    {
        return Bukkit.getConsoleSender().isConversing();
    }

    /**
     * Accepts input into the active conversation. If no conversation is in
     * progress, this method does nothing.
     *
     * @param input The input message into the conversation
     */
    @Override
    public void acceptConversationInput(@NotNull String input)
    {
        Bukkit.getConsoleSender().acceptConversationInput(input);
    }

    /**
     * Enters into a dialog with a Conversation object.
     *
     * @param conversation The conversation to begin
     * @return True if the conversation should proceed, false if it has been
     * enqueued
     */
    @Override
    public boolean beginConversation(@NotNull Conversation conversation)
    {
        return Bukkit.getConsoleSender().beginConversation(conversation);
    }

    /**
     * Abandons an active conversation.
     *
     * @param conversation The conversation to abandon
     */
    @Override
    public void abandonConversation(@NotNull Conversation conversation)
    {
        Bukkit.getConsoleSender().abandonConversation(conversation);
    }

    /**
     * Abandons an active conversation.
     *
     * @param conversation The conversation to abandon
     * @param details      Details about why the conversation was abandoned
     */
    @Override
    public void abandonConversation(@NotNull Conversation conversation,@NotNull ConversationAbandonedEvent details)
    {
        Bukkit.getConsoleSender().abandonConversation(conversation,details);
    }

    /**
     * Sends this sender a message raw
     *
     * @param message Message to be displayed
     */
    @Override
    public void sendRawMessage(@NotNull String message)
    {
        sendMessage(message);
    }

    /**
     * Sends this sender a message raw
     *
     * @param sender  The sender of this message
     * @param message Message to be displayed
     */
    @Override
    public void sendRawMessage(@Nullable UUID sender,@NotNull String message)
    {
        sendMessage(message);
    }

    /**
     * Checks if this object contains an override for the specified
     * permission, by fully qualified name
     *
     * @param name Name of the permission
     * @return true if the permission is set, otherwise false
     */
    @Override
    public boolean isPermissionSet(@NotNull String name)
    {
        return Bukkit.getConsoleSender().isPermissionSet(name);
    }

    /**
     * Checks if this object contains an override for the specified {@link
     * Permission}
     *
     * @param perm Permission to check
     * @return true if the permission is set, otherwise false
     */
    @Override
    public boolean isPermissionSet(@NotNull Permission perm)
    {
        return Bukkit.getConsoleSender().isPermissionSet(perm);
    }

    /**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned.
     *
     * @param name Name of the permission
     * @return Value of the permission
     */
    @Override
    public boolean hasPermission(@NotNull String name)
    {
        return Bukkit.getConsoleSender().hasPermission(name);
    }

    /**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned
     *
     * @param perm Permission to get
     * @return Value of the permission
     */
    @Override
    public boolean hasPermission(@NotNull Permission perm)
    {
        return Bukkit.getConsoleSender().hasPermission(perm);
    }

    /**
     * Adds a new {@link PermissionAttachment} with a single permission by
     * name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param name   Name of the permission to attach
     * @param value  Value of the permission
     * @return The PermissionAttachment that was just created
     */
    @NotNull
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin,@NotNull String name,boolean value)
    {
        return Bukkit.getConsoleSender().addAttachment(plugin,name,value);
    }

    /**
     * Adds a new empty {@link PermissionAttachment} to this object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @return The PermissionAttachment that was just created
     */
    @NotNull
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin)
    {
        return Bukkit.getConsoleSender().addAttachment(plugin);
    }

    /**
     * Temporarily adds a new {@link PermissionAttachment} with a single
     * permission by name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param name   Name of the permission to attach
     * @param value  Value of the permission
     * @param ticks  Amount of ticks to automatically remove this attachment
     *               after
     * @return The PermissionAttachment that was just created
     */
    @Nullable
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin,@NotNull String name,boolean value,int ticks)
    {
        return Bukkit.getConsoleSender().addAttachment(plugin,name,value,ticks);
    }

    /**
     * Temporarily adds a new empty {@link PermissionAttachment} to this
     * object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param ticks  Amount of ticks to automatically remove this attachment
     *               after
     * @return The PermissionAttachment that was just created
     */
    @Nullable
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin,int ticks)
    {
        return Bukkit.getConsoleSender().addAttachment(plugin,ticks);
    }

    /**
     * Removes the given {@link PermissionAttachment} from this object
     *
     * @param attachment Attachment to remove
     * @throws IllegalArgumentException Thrown when the specified attachment
     *                                  isn't part of this object
     */
    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment)
    {
        Bukkit.getConsoleSender().removeAttachment(attachment);
    }

    /**
     * Recalculates the permissions for this object, if the attachments have
     * changed values.
     * <p>
     * This should very rarely need to be called from a plugin.
     */
    @Override
    public void recalculatePermissions()
    {

    }

    /**
     * Gets a set containing all of the permissions currently in effect by
     * this object
     *
     * @return Set of currently effective permissions
     */
    @NotNull
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions()
    {
        return Bukkit.getConsoleSender().getEffectivePermissions();
    }

    /**
     * Checks if this object is a server operator
     *
     * @return true if this is an operator, otherwise false
     */
    @Override
    public boolean isOp()
    {
        return Bukkit.getConsoleSender().isOp();
    }

    /**
     * Sets the operator status of this object
     *
     * @param value New operator value
     */
    @Override
    public void setOp(boolean value)
    {
        Bukkit.getConsoleSender().setOp(value);
    }
}
