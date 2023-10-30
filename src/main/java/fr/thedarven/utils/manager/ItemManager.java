package fr.thedarven.utils.manager;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.Manager;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.math.BigInteger;
import java.util.UUID;

public class ItemManager extends Manager {

    public ItemManager(TaupeGun main) {
        super(main);
    }

    public ItemStack getTaggedItemStack(Material material, short data) {
        ItemStack item = new ItemStack(org.bukkit.Material.DIRT,1);

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("uuid", UUID.randomUUID().toString());
        nmsItem.setTag(tag);
        item = CraftItemStack.asBukkitCopy(nmsItem);

        item.setType(material);
        item.setDurability(data);

        return item;
    }

    public ItemStack addTagOnItemStack(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getTag();
        tag.setString("uuid", UUID.randomUUID().toString());
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    /**
     * @author https://www.spigotmc.org/threads/how-to-serialize-itemstack-inventory-with-attributestorage.152931/#post-1625561
     *
     * Item to Base 64
     * @param item
     * @return
     */
    public String toBase64(ItemStack item) {
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
     * @author https://www.spigotmc.org/threads/how-to-serialize-itemstack-inventory-with-attributestorage.152931/#post-1625561
     *
     * Item from Base64
     * @param data
     * @return
     */
    public ItemStack fromBase64(String data) {
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

}
