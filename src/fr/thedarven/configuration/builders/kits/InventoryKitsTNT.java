package fr.thedarven.configuration.builders.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryKitsTNT extends InventoryKitsElement {

	public InventoryKitsTNT(String pName) {
		super(pName);
		reloadInventory();
	}
	
	@SuppressWarnings("deprecation")
	private void reloadInventory() {
		Inventory inv = this.getInventory();
		inv.addItem(new ItemStack(Material.TNT,5));
		inv.addItem(new ItemStack(Material.FLINT_AND_STEEL,1));
		inv.addItem(new ItemStack(Material.MONSTER_EGG, 1, EntityType.CREEPER.getTypeId()));
	}

}
