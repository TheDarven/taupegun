package fr.thedarven.scenarios.kits;

import fr.thedarven.TaupeGun;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryKitsTNT extends InventoryKitsElement {

	public InventoryKitsTNT(TaupeGun main, String name, InventoryKits parent) {
		super(main, parent, null);
		reloadInventory();
	}

	@Override
	public void reloadInventory() {
		Inventory inv = this.getInventory();

		inv.addItem(new ItemStack(Material.TNT,5));

		inv.addItem(new ItemStack(Material.FLINT_AND_STEEL,1));
		
		inv.addItem(new ItemStack(Material.MONSTER_EGG, 1, EntityType.CREEPER.getTypeId()));
	}

}
