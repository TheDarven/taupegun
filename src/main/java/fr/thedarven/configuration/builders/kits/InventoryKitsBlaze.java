package fr.thedarven.configuration.builders.kits;

import fr.thedarven.configuration.builders.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class InventoryKitsBlaze extends InventoryKitsElement {

	public InventoryKitsBlaze(String pName, InventoryKits parent) {
		super(pName, parent);
		reloadInventory();
	}
	
	/**
	 * Recharge les objets de l'inventaire
	 */
	@SuppressWarnings("deprecation")
	public void reloadInventory() {
		Inventory inv = this.getInventory();
		inv.addItem(new ItemStack(Material.MONSTER_EGG, 3, EntityType.BLAZE.getTypeId()));
		ItemStack fire = new ItemStack(Material.ENCHANTED_BOOK, 1);
		EnchantmentStorageMeta fireM = (EnchantmentStorageMeta) fire.getItemMeta();
		fireM.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
		fire.setItemMeta(fireM);
		inv.addItem(fire);
	}

}