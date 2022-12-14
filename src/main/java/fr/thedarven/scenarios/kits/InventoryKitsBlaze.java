package fr.thedarven.scenarios.kits;

import fr.thedarven.TaupeGun;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class InventoryKitsBlaze extends InventoryKitsElement {

	public InventoryKitsBlaze(TaupeGun main, String name, InventoryKits parent) {
		super(main, parent, null);
		reloadInventory();
	}

	@Override
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