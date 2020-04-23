package fr.thedarven.configuration.builders;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;


public class InventoryStartItem extends InventoryGUI{

	public InventoryStartItem() {
		super("Stuff de départ", "Configuration du stuff de départ.", "MENU_STARTER_KIT", 6, Material.CHEST, InventoryRegister.menu, 8);
		initItem();
	}
	
	/**
	 * Pour initier les items
	 */
	private void initItem() {
		int i = 0;
		ItemStack verre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta verreM = verre.getItemMeta();
		verreM.setDisplayName("§f");
		verre.setItemMeta(verreM);
		
		for(i=4; i<9; i++) {
			getInventory().setItem(i, verre);
		}
		for(i=45; i<53; i++) {
			getInventory().setItem(i, verre);
		}
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(getInventory())) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			
			if(click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					e.setCancelled(true);
					p.openInventory(getParent().getInventory());
					return;
				}
	
				if(e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE) && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals("§f")){
					e.setCancelled(true);
				}
			}
		}
	}
	
}
