package fr.thedarven.configuration.builders.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryIncrement;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.api.Title;

public class InventoryKits extends InventoryIncrement {
	
	public InventoryKits(InventoryGUI pInventoryGUI) {
		super("Kits", "Menu des kits.", 2, Material.ENDER_CHEST, InventoryRegister.menu, 4);
	}

	public void reloadInventory() {
		for(InventoryGUI inv : getChilds()) {
			inv.getParent().removeItem(inv);
		}
		int i = 0;
		for(InventoryGUI inv : getChilds()) {
			if(inv instanceof InventoryKitsElement) {
				modifiyPosition(inv,i);
				i++;
			}else if(getChilds().size() < 10){
				modifiyPosition(inv,getChilds().size()-1);
			}
		}
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
			final Player p = (Player) e.getWhoClicked();
			final PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			
			if(click(p, EnumConfiguration.OPTION) && e.getCurrentItem().equals(InventoryRegister.addkits.getItem())) {
				e.setCancelled(true);
				new AnvilGUI(TaupeGun.getInstance(),p, new AnvilGUI.AnvilClickHandler() {
					
					@Override
				    public boolean onClick(AnvilGUI menu, String text) {
				    	pl.setCreateKitName(text);
				    	Bukkit.getScheduler().runTask(TaupeGun.getInstance(), new Runnable() {
	
				    		@Override
				    		public void run() {
					    		if(pl.getCreateKitName().length() > 16){
					    			p.closeInventory();
					    			Title.sendActionBar(p, ChatColor.RED+"Le nom du kit ne doit pas dépasser 16 caractères.");
					    			pl.setCreateKitName(null);
					    			return;
					    		}
					    		
					    		for(InventoryGUI inv : getChilds()) {
					    			if(inv instanceof InventoryKitsElement && inv.getName().equals(pl.getCreateKitName())) {
					    				p.openInventory(InventoryRegister.kits.getInventory());
						    			Title.sendActionBar(p, ChatColor.RED+"Le nom est déjà utilisé pour un autre kit.");
						    			pl.setCreateKitName(null);
						    			return;
					    			}
					    		}
					    		
					    		InventoryKitsElement kit = new InventoryKitsElement(pl.getCreateKitName());
					    		new InventoryDeleteKits(kit);
					    		Title.sendActionBar(p, ChatColor.GREEN+" Le kit "+ChatColor.YELLOW+ChatColor.BOLD+pl.getCreateKitName()+ChatColor.RESET+ChatColor.GREEN+" a été crée avec succès.");
								pl.setCreateKitName(null);
								p.openInventory(InventoryRegister.kits.getLastChild().getInventory());
					    	}
				    	});
				    	return true;
				    }
				}).setInputName("Nom du kit").open();
			}else if(e.getClickedInventory().equals(getInventory())) {
				
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
					e.setCancelled(true);
					p.openInventory(getParent().getInventory());
					return;
				}else {
					for(InventoryGUI inventoryGUI : childs) {
						if(inventoryGUI.getItem().equals(e.getCurrentItem())) {
							e.setCancelled(true);
							p.openInventory(inventoryGUI.getInventory());
							delayClick(pl);
							return;
						}
					}
				}
			}
		}
	}
	
}
