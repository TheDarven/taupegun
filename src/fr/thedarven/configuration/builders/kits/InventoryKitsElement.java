package fr.thedarven.configuration.builders.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.PlayerTaupe;

public class InventoryKitsElement extends InventoryGUI {
	
	protected static ArrayList<InventoryKitsElement> inventory = new ArrayList<>();
	
	public InventoryKitsElement(String pName) {
		super(pName, "", 2, Material.CHEST, InventoryRegister.kits, 0);
		inventory.add(this);
		initItem();
		reloadItem();
		InventoryRegister.kits.reloadInventory();
	}

	private void initItem() {
		int i = 0;
		ItemStack verre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta verreM = verre.getItemMeta();
		verreM.setDisplayName("§f");
		verre.setItemMeta(verreM);
		
		for(i=10; i<17; i++) {
			getInventory().setItem(i, verre);
		}
	}
	
	static public void removeKit(String pNom) {
		for(int i=0; i<inventory.size(); i++) {
			if(inventory.get(i).getName().equals(pNom)) {
				inventory.get(i).getParent().getChilds().remove(inventory.get(i));
				inventory.get(i).getParent().removeItem(inventory.get(i));
				InventoryRegister.kits.reloadInventory();
				inventory.remove(i);
				return;
			}
		}
	}
	
	public void reloadItem(){
		ItemStack item = getItem();
		int hashCode = item.hashCode();
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(getName());
		item.setItemMeta(itemM);
		getParent().modifyItemstack(hashCode, item);			
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			if(e.getClickedInventory().equals(getInventory())) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
					e.setCancelled(true);
					p.openInventory(getParent().getInventory());
					return;
				}else if(!click(p, EnumConfiguration.OPTION)) {
					e.setCancelled(true);
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
					if(!e.getCurrentItem().getType().equals(Material.AIR)) {
						if(e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE) && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals("§f")){
							e.setCancelled(true);
						}
						delayClick(pl);
					}	
				}
			}
		}
	}
	
}
