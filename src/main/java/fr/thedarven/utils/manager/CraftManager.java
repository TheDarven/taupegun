package fr.thedarven.utils.manager;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftManager extends Manager {

    public CraftManager(TaupeGun main) {
        super(main);
    }

    public void loadCrafts(){
        ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta goldenHeadM = goldenHead.getItemMeta();
        goldenHeadM.addEnchant(Enchantment.DURABILITY, 1, false);
        goldenHeadM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        goldenHeadM.setDisplayName("§6Golden Head");
        goldenHead.setItemMeta(goldenHeadM);

        ShapedRecipe recipe = new ShapedRecipe(goldenHead);
        recipe.shape("OOO", "OTO", "OOO");
        recipe.setIngredient('O', Material.GOLD_INGOT);
        recipe.setIngredient('T', Material.SKULL_ITEM, (short) 3);
        this.main.getServer().addRecipe(recipe);
    }
}
