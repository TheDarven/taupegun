package fr.thedarven.utils.api.skull;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import fr.thedarven.utils.api.Reflection;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

/**
 * Creates and modifies custom Skull item
 *
 * @author TheDarven
 */
public class Skull {

    /**
     * Creates a Skull item with specific url skin
     *
     * @param url the url
     * @return the skull Itemstack
     */
    public static ItemStack getCustomSkull(String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        return updateSkullSkin(url, head);
    }

    /**
     * Modifies a Skull item with specific url skin
     *
     * @param url the url
     * @param head the skull itemstack
     * @return the skull Itemstack modified
     */
    public static ItemStack updateSkullSkin(String url, ItemStack head) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();

        Base64 base64 = new Base64();
        byte[] encodedData = base64.encode(String.format("{textures: {SKIN: {url: \"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));

        ItemMeta headMeta = head.getItemMeta();
        try {
            Reflection.setValue(headMeta, "profile", profile);
        } catch (IllegalAccessException | NoSuchFieldException ignored) { }
        head.setItemMeta(headMeta);
        return head;
    }

    /**
     * Creates a Skull item with Player skin
     *
     * @param playerName
     * @return
     */
    public static ItemStack getPlayerHead(String playerName) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(playerName);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
