package fr.thedarven.configuration.builders;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.PlayerTaupe;
import net.md_5.bungee.api.ChatColor;

public abstract class InventoryDelete extends InventoryGUI {

	public InventoryDelete(InventoryGUI pInventoryGUI, String pName, int pPosition) {
		super(pName, "", 1, Material.STAINED_CLAY, pInventoryGUI, pPosition, (byte) 14);
		initItem();
	}
	
	private void initItem() {
		getInventory().clear();
		
		ItemStack confirmer = new ItemStack(Material.STAINED_CLAY, 1, (byte) 13);
		ItemMeta confirmerM = confirmer.getItemMeta();
		confirmerM.setDisplayName(ChatColor.GREEN+"✔ Confirmer");
		confirmer.setItemMeta(confirmerM);
		getInventory().setItem(2, confirmer);
		
		ItemStack annuler = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
		ItemMeta annulerM = annuler.getItemMeta();
		annulerM.setDisplayName(ChatColor.RED+"✘ Annuler");
		annuler.setItemMeta(annulerM);
		getInventory().setItem(6, annuler);
	}
	
	protected abstract void deleteElement(Player p);
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)){
					if(e.getCurrentItem().getDurability() == 13) {
						deleteElement(p);
					}else if(e.getCurrentItem().getDurability() == 14) {
						p.openInventory(getParent().getInventory());
					}
				}
				delayClick(pl);
			}
		}
	}
	
}
