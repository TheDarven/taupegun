package fr.thedarven.utils.api.titles;

import fr.thedarven.utils.api.thedarven.Reflection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.entity.Player;

/**
 * Changes the content of list header and list footer
 *
 * @author TheDarven
 */
public class TabMessage {

    private final String header;
    private final String footer;

    public TabMessage(String header, String footer) {
        this.header = header;
        this.footer = footer;
    }

    /**
     * Sends the modification to a player
     *
     * @param player the player who receives the change
     */
    public TabMessage sendTabTitle(Player player) {
        IChatBaseComponent headerJson = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + this.header +"\"}");
        IChatBaseComponent footerJson = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + this.footer +"\"}");
        PacketPlayOutPlayerListHeaderFooter tabPacket = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Reflection.setValue(tabPacket, "a", headerJson);
            Reflection.setValue(tabPacket, "b", footerJson);
            Reflection.sendPackets(player, tabPacket);
        } catch (Exception ignored) { }
        return this;
    }

}

