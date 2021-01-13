package fr.thedarven.utils;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftManager extends Manager {

    public CraftManager(TaupeGun main) {
        super(main);
        loadCrafts();
    }

    private void loadCrafts(){
        // Recette
        ItemStack GoldenHead = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta GoldenHeadM = GoldenHead.getItemMeta();
        GoldenHeadM.addEnchant(Enchantment.DURABILITY, 1, false);
        GoldenHeadM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        GoldenHeadM.setDisplayName(ChatColor.GOLD+"Golden Head");
        GoldenHead.setItemMeta(GoldenHeadM);

        ShapedRecipe recette = new ShapedRecipe(GoldenHead);
        recette.shape("OOO", "OTO", "OOO");
        recette.setIngredient('O', Material.GOLD_INGOT);
        recette.setIngredient('T', Material.SKULL_ITEM, (short) 3);
        TaupeGun.getInstance().getServer().addRecipe(recette);
    }
}
