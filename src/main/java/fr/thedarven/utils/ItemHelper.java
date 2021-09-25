package fr.thedarven.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemHelper {

    public static boolean isNullOrAir(ItemStack item) {
        return Objects.isNull(item) || item.getType() == Material.AIR;
    }

}
