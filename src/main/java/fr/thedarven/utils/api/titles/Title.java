package fr.thedarven.utils.api.titles;

import fr.thedarven.utils.api.thedarven.Reflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Sends title, action bar message and tab message
 *
 * @author TheDarven
 */
public class Title {

    private final String title;
    private final String subtitle;
    private final int duration;

    public Title(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.duration = 20;
    }

    public Title(String title, String subtitle, int duration) {
        this.title = title;
        this.subtitle = subtitle;
        this.duration = duration;
    }

    /**
     * Sends the title to a Player
     *
     * @param player the receiving player
     */
    public Title sendTitle(Player player) {
        Reflection.sendPackets(player, new PacketPlayOutTitle(10, this.duration, 10));

        PacketPlayOutTitle packetSubtitle;
        if (Objects.nonNull(this.title)) {
            packetSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, CraftChatMessage.fromString(this.title)[0]);
            Reflection.sendPackets(player, packetSubtitle);
        }

        if (Objects.nonNull(this.subtitle)) {
            packetSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, CraftChatMessage.fromString(this.subtitle)[0]);
            Reflection.sendPackets(player, packetSubtitle);
        }
        return this;
    }

}
