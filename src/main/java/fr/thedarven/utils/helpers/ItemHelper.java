package fr.thedarven.utils.helpers;

import fr.thedarven.utils.api.skull.Skull;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemHelper {

    public static boolean isNullOrAir(ItemStack item) {
        return Objects.isNull(item) || item.getType() == Material.AIR;
    }

    /**
     * Pour couper une phrase en plusieurs lignes
     *
     * @param description Le message à couper
     * @param color       La couleur à mettre au début de chaque ligne
     * @param size        La taille maximale de chaque ligne
     * @return La phrase coupé en plusieurs ligne
     */
    public static List<String> toLoreItem(String description, String color, int size) {
        if (Objects.isNull(description)) {
            return new ArrayList<>();
        }

        size = Math.max(size, 25);

        String[] items = description.split(" ");
        List<String> list = new ArrayList<>();

        if (items.length > 0) {
            StringBuilder lines = new StringBuilder(color + items[0]);

            int nbItems = items.length;
            for (int i = 1; i < nbItems; i++) {
                String item = items[i];
                if ((lines.length() + 1 + item.length()) > size) {
                    list.add(lines.toString());
                    lines = new StringBuilder(color + item);
                } else {
                    lines.append(" ").append(item);
                }
            }
            if (lines.length() > 0) {
                list.add(lines.toString());
            }
        }

        return list;
    }

    public static ItemStack getTaggedItemStack(Material material, short data) {
        ItemStack item = addTagOnItemStack(new ItemStack(org.bukkit.Material.DIRT, 1));
        item.setType(material);
        item.setDurability(data);
        return item;
    }

    public static ItemStack addTagOnItemStack(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getTag();
        boolean hasTag = tag != null;
        if (!hasTag) {
            tag = new NBTTagCompound();
        }
        tag.setString("uuid", UUID.randomUUID().toString());
        if (!hasTag) {
            nmsItem.setTag(tag);
        }
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static boolean isTaggedItem(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (!nmsItem.hasTag()) {
            return false;
        }
        NBTTagCompound tag = nmsItem.getTag();
        return tag.hasKey("uuid");
    }

    /**
     * @param item
     * @return
     * @author https://www.spigotmc.org/threads/how-to-serialize-itemstack-inventory-with-attributestorage.152931/#post-1625561
     * <p>
     * Item to Base 64
     */
    public static String toBase64(ItemStack item) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);

        NBTTagList nbtTagListItems = new NBTTagList();
        NBTTagCompound nbtTagCompoundItem = new NBTTagCompound();

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        nmsItem.save(nbtTagCompoundItem);

        nbtTagListItems.add(nbtTagCompoundItem);

        try {
            NBTCompressedStreamTools.a(nbtTagCompoundItem, (DataOutput) dataOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BigInteger(1, outputStream.toByteArray()).toString(32);
    }

    /**
     * @param data
     * @return
     * @author https://www.spigotmc.org/threads/how-to-serialize-itemstack-inventory-with-attributestorage.152931/#post-1625561
     * <p>
     * Item from Base64
     */
    public static ItemStack fromBase64(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());

        NBTTagCompound nbtTagCompoundRoot = null;
        try {
            nbtTagCompoundRoot = NBTCompressedStreamTools.a(new DataInputStream(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = net.minecraft.server.v1_8_R3.ItemStack.createStack(nbtTagCompoundRoot);
        ItemStack item = CraftItemStack.asBukkitCopy(nmsItem);

        return item;
    }

    /**
     * Creates a Skull item with Player skin and custom name
     *
     * @param playerName The player name
     * @param customHeadName The custom name of the item
     * @return
     */
    public static ItemStack getPlayerHeadWithName(String playerName, String customHeadName) {
        ItemStack head = Skull.getPlayerHead(playerName);
        SkullMeta headM = (SkullMeta) head.getItemMeta();
        headM.setDisplayName(customHeadName);
        head.setItemMeta(headM);
        return head;
    }

}
