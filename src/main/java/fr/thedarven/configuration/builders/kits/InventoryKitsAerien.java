package fr.thedarven.configuration.builders.kits;

import fr.thedarven.configuration.builders.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryKitsAerien extends InventoryKitsElement {

	public InventoryKitsAerien(String pName, InventoryKits parent) {
		super(pName, parent);
		reloadInventory();
	}
	
	/**
	 * Recharge les objets de l'inventaire
	 */
	public void reloadInventory() {
		Inventory inv = this.getInventory();
		
		ItemStack arc = new ItemStack(Material.BOW, 1);
		ItemMeta arcM = arc.getItemMeta();
		arcM.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
		arc.setItemMeta(arcM);
		inv.addItem(arc);
		inv.addItem(new ItemStack(Material.ENDER_PEARL, 4));
		ItemStack falling = new ItemStack(Material.ENCHANTED_BOOK, 1);
		EnchantmentStorageMeta fallingM = (EnchantmentStorageMeta) falling.getItemMeta();
		fallingM.addStoredEnchant(Enchantment.PROTECTION_FALL, 4, true);
		falling.setItemMeta(fallingM);
		inv.addItem(falling);
	}

}