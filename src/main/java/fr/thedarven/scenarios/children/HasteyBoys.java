package fr.thedarven.scenarios.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class HasteyBoys extends OptionBoolean {

    private final List<Material> TOOLS = Arrays.asList(
            Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.GOLD_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE,
            Material.WOOD_AXE, Material.STONE_AXE, Material.GOLD_AXE, Material.IRON_AXE, Material.DIAMOND_AXE,
            Material.WOOD_HOE, Material.STONE_HOE, Material.GOLD_HOE, Material.IRON_HOE, Material.DIAMOND_HOE,
            Material.WOOD_SPADE, Material.STONE_SPADE, Material.GOLD_SPADE, Material.IRON_SPADE, Material.DIAMOND_SPADE);

    public HasteyBoys(TaupeGun main, InventoryGUI parent) {
        super(
                main,
                "Hastey Boys",
                "Les outils possèdent automatiquement l'enchantement Efficacité III.",
                "MENU_CONFIGURATION_SCENARIO_HASTEY_BOYS",
                Material.ENCHANTED_BOOK,
                parent,
                false);
    }

    @EventHandler
    public void onToolCrafting(PrepareItemCraftEvent event) {
        if (!this.value) {
            return;
        }

        ItemStack result = event.getInventory().getResult();
        if (result == null || TOOLS.stream().noneMatch(tool -> tool == result.getType())) {
            return;
        }

        ItemMeta resultM = result.getItemMeta();
        if (resultM.hasEnchant(Enchantment.DIG_SPEED)) {
            return;
        }

        resultM.addEnchant(Enchantment.DIG_SPEED, 3, true);
        result.setItemMeta(resultM);
        event.getInventory().setResult(result);
    }

}
