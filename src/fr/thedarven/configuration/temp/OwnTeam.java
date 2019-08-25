package fr.thedarven.configuration.temp;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;
import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.PlayerTaupe;

public class OwnTeam extends OptionBoolean{

	public OwnTeam(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pPosition, pValue);
	}
	
	public OwnTeam(String pName, String pDescription, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pValue);
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
					p.openInventory(this.getParent().getInventory());
					return;
				}

				if(e.getSlot() == 3 && this.value) {
					this.value = false;
					super.reloadItem();
					/* for(Player player : Bukkit.getOnlinePlayers()) {
						ItemStack banner = new ItemStack(Material.BANNER,(byte) 15);
						
					} */
				}else if(e.getSlot() == 5 && !this.value) {
					this.value = true;
					super.reloadItem();
				}
				delayClick(pl);
			}
		}
	}
	
	
}
