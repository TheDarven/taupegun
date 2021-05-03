package fr.thedarven.scenarios.childs;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.helper.InventoryGiveItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryStartItem extends InventoryGUI implements AdminConfiguration, InventoryGiveItem {

	public InventoryStartItem(TaupeGun main, InventoryGUI parent) {
		super(main,"Stuff de départ", "Configuration du stuff de départ.", "MENU_STARTER_KIT", 6, Material.CHEST, parent, 8);
		initItem();
	}
	
	/**
	 * Pour initier les items
	 */
	private void initItem() {
		int i = 0;
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta glassM = glass.getItemMeta();
		glassM.setDisplayName("§f");
		glass.setItemMeta(glassM);
		
		for (i = 4; i < 9; i++) {
			getInventory().setItem(i, glass);
		}

		for (i = 45; i < 53; i++) {
			getInventory().setItem(i, glass);
		}
	}

	@Override
	public void giveItems(Player player) {
		Inventory currentInventory = this.getInventory();
		Inventory playerInventory = player.getInventory();

		for (int i = 0; i < 45; i++) {
			if (i < 4) {
				playerInventory.setItem(39 - i, currentInventory.getItem(i));
			} else if (i < 36) {
				playerInventory.setItem(i, currentInventory.getItem(i));
			} else {
				playerInventory.setItem(i - 36, currentInventory.getItem(i));
			}
		}
	}

	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		if (openChildInventory(e.getCurrentItem(), player, pl))
			return;

		if (isLockedCaseItem(e.getCurrentItem()))
			return;

		e.setCancelled(false);
	}
	
}
