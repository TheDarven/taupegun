package fr.thedarven.configuration.builders;

import fr.thedarven.configuration.builders.helper.ClickCooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.models.EnumConfiguration;
import fr.thedarven.models.PlayerTaupe;


public class InventoryStartItem extends InventoryGUI implements ClickCooldown {

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
		
		for (i=4; i<9; i++) {
			getInventory().setItem(i, glass);
		}
		for (i=45; i<53; i++) {
			getInventory().setItem(i, glass);
		}
	}

	/**
	 * Pour donner les items de l'ivnentaire à un joueur
	 *
	 * @param player Le joueur
	 */
	public void giveItems(Player player) {
		for (int i = 0; i < 45; i++) {
			if (i < 4) {
				player.getInventory().setItem(39-i, this.getInventory().getItem(i));
			} else if (i < 36) {
				player.getInventory().setItem(i, this.getInventory().getItem(i));
			}else {
				player.getInventory().setItem(i-36, this.getInventory().getItem(i));
			}
		}
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(getInventory())) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			
			if (click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if (e.getCurrentItem().getType() == Material.REDSTONE && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					e.setCancelled(true);
					p.openInventory(getParent().getInventory());
					return;
				}
	
				if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals("§f")){
					e.setCancelled(true);
				}
			}
		}
	}
	
}
