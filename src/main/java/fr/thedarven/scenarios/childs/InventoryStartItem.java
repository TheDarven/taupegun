package fr.thedarven.scenarios.childs;

import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.ClickCooldown;
import fr.thedarven.scenarios.helper.InventoryGiveItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class InventoryStartItem extends InventoryGUI implements ClickCooldown, InventoryGiveItem {

	public InventoryStartItem(InventoryGUI parent) {
		super("Stuff de départ", "Configuration du stuff de départ.", "MENU_STARTER_KIT", 6, Material.CHEST, parent, 8);
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
	
	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (e.getWhoClicked() instanceof Player && Objects.nonNull(e.getClickedInventory()) && e.getClickedInventory().equals(getInventory())) {
			Player player = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
			
			if (click(player,EnumConfiguration.OPTION) && e.getCurrentItem().getType() != Material.AIR && pl.getCanClick()) {
				if (isReturnItem(e.getCurrentItem(), e.getRawSlot())) {
					e.setCancelled(true);
					player.openInventory(getParent().getInventory());
					return;
				}
	
				if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals("§f")){
					e.setCancelled(true);
				}
			}
		}
	}
	
}
