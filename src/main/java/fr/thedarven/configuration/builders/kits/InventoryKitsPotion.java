package fr.thedarven.configuration.builders.kits;

import fr.thedarven.configuration.builders.InventoryGUI;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class InventoryKitsPotion extends InventoryKitsElement {

	public InventoryKitsPotion(String pName, InventoryKits parent) {
		super(pName, parent);
		reloadInventory();
	}
	
	/**
	 * Recharge les objets de l'inventaire
	 */
	@SuppressWarnings("deprecation")
	public void reloadInventory() {
		Inventory inv = this.getInventory();
		
		Potion poison = new Potion(PotionType.POISON, 1, true);
		inv.addItem(poison.toItemStack(1));
		Potion slowness = new Potion(PotionType.SLOWNESS, 1, true);
		inv.addItem(slowness.toItemStack(1));
		Potion weakness = new Potion(PotionType.WEAKNESS, 1, true);
		inv.addItem(weakness.toItemStack(1));
		Potion damage = new Potion(PotionType.INSTANT_DAMAGE, 1, true);
		inv.addItem(damage.toItemStack(1));
	}

}
