package fr.thedarven.items;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemManager extends Manager {

    public ItemManager(TaupeGun main) {
        super(main);
    }

    public ItemStack getTaggedItemStack(Material material, short data) {
        ItemStack item = new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIRT,1);

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("uuid", UUID.randomUUID().toString());
        nmsItem.setTag(tag);
        item = CraftItemStack.asBukkitCopy(nmsItem);

        item.setType(material);
        item.setDurability(data);

        return item;
    }

}
