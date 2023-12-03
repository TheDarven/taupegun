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

    public static final String UP_HEAD_URL = "https://textures.minecraft.net/texture/a99aaf2456a6122de8f6b62683f2bc2eed9abb81fd5bea1b4c23a58156b669";
    public static final String LEFT_HEAD_URL = "https://textures.minecraft.net/texture/5f133e91919db0acefdc272d67fd87b4be88dc44a958958824474e21e06d53e6";
    public static final String DOWN_HEAD_URL = "https://textures.minecraft.net/texture/3912d45b1c78cc22452723ee66ba2d15777cc288568d6c1b62a545b29c7187";
    public static final String RIGHT_HEAD_URL = "https://textures.minecraft.net/texture/e3fc52264d8ad9e654f415bef01a23947edbccccf649373289bea4d149541f70";

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
     * @param url  the url
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
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        head.setItemMeta(headMeta);
        return head;
    }

    /**
     * Creates a Skull item with Player skin
     *
     * @param playerName The player name
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
