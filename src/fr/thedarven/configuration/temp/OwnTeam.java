package fr.thedarven.configuration.temp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;

public class OwnTeam extends OptionBoolean{

	public OwnTeam(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pPosition, pValue);
		actionBanner();
	}
	
	public OwnTeam(String pName, String pDescription, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pValue);
		actionBanner();
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
					for(Player player : Bukkit.getOnlinePlayers()) {
						removeBanner(player);
					}
				}else if(e.getSlot() == 5 && !this.value) {
					this.value = true;
					super.reloadItem();
					for(Player player : Bukkit.getOnlinePlayers())
						giveBanner(player);
				}
				delayClick(pl);
			}
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		removeBanner(e.getPlayer());
		if(TaupeGun.etat.equals(EnumGame.LOBBY) && value) {
			giveBanner(e.getPlayer());
		}
	}
	
	private void actionBanner() {
		if(TaupeGun.etat.equals(EnumGame.LOBBY)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				removeBanner(p);
				if(value)
					giveBanner(p);
			}
		}
	}
	
	private void removeBanner(Player p) {
		Inventory playerInv = p.getInventory();
		for(int i=0; i<playerInv.getSize(); i++) {
			if(playerInv.getItem(i) != null && playerInv.getItem(i).getType() == Material.BANNER && playerInv.getItem(i).getItemMeta().getDisplayName().equals("§eChoix de l'équipe")) {
				playerInv.setItem(i, new ItemStack(Material.AIR));
			}
		}
	}
	
	private void giveBanner(Player p) {
		ItemStack banner = new ItemStack(Material.BANNER, 1, (byte) 15);
		ItemMeta bannerM = banner.getItemMeta();
		bannerM.setDisplayName("§eChoix de l'équipe");
		banner.setItemMeta(bannerM);
		p.getInventory().setItem(4, banner);
	}
	
	
}
