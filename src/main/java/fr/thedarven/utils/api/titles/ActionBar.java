package fr.thedarven.utils.api.titles;

import fr.thedarven.utils.api.Reflection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.entity.Player;

/**
 * Sends action bar message
 *
 * @author TheDarven
 */
public class ActionBar {

    private final String message;

    public ActionBar(String message) {
        this.message = message;
    }

    /**
     * Sends the action bar message to a Player
     *
     * @param player the receiving player
     */
    public ActionBar sendActionBar(Player player) {
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + this.message + "\"}");
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatBaseComponent, (byte) 2);
        Reflection.sendPackets(player, packetPlayOutChat);
        return this;
    }

}

