package fr.thedarven.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;

public class ScenariosItemInteract implements Listener{

	public ScenariosItemInteract() {}

	@EventHandler
	public void onItemUse(PlayerInteractEvent e) {
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			Player p = e.getPlayer();
			ItemStack playerItem = p.getItemInHand();
			
			if(playerItem != null && playerItem.getType().equals(Material.BEACON) && playerItem.getItemMeta().getDisplayName().equals(getFormattedScenariosItemName())) {
				if(EnumGameState.isCurrentState(EnumGameState.LOBBY))
					UtilsClass.openConfigInventory(p);
				e.setCancelled(true);
			}
		}
	}
	
	/**
	 * Donne ou enlève le beacon aux joueurs
	 * 
	 * @param exName L'ancien nom de l'item beacon
	 */
	public static void actionBeacon(String exName) {
		for(Player p: Bukkit.getOnlinePlayers()) {
			removeBeacon(p, exName);
			if(EnumGameState.isCurrentState(EnumGameState.LOBBY) && p.isOp())
				giveBeacon(p);
		}
	}
	
	/**
	 * Donne ou enlève le beacon à un joueur
	 * 
	 * @param p Le joueur dont on doit donner le beacon
	 */
	public static void actionBeacon(Player p) {
		removeBeacon(p, getFormattedScenariosItemName());
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY))
			giveBeacon(p);
	}

	/**
	 * Pour supprimer le beacon d'un joueur
	 * 
	 * @param p Le joueur dont on doit supprimer le beacon
	 */
	public static void removeBeacon(Player p) {
		Inventory playerInv = p.getInventory();
		for(int i=0; i<playerInv.getSize(); i++) {
			if(playerInv.getItem(i) != null && playerInv.getItem(i).getType() == Material.BEACON && playerInv.getItem(i).getItemMeta().getDisplayName().equals(getFormattedScenariosItemName())) {
				playerInv.setItem(i, new ItemStack(Material.AIR));
			}
		}
	}
	
	/**
	 * Pour supprimer le beacon d'un joueur car la langue selectionnée a changé
	 * 
	 * @param p Le joueur dont on doit supprimer le beacon
	 * @param exName L'ancien nom de l'item beacon
	 */
	public static void removeBeacon(Player p, String exName) {
		Inventory playerInv = p.getInventory();
		for(int i=0; i<playerInv.getSize(); i++) {
			if(playerInv.getItem(i) != null && playerInv.getItem(i).getType() == Material.BEACON && playerInv.getItem(i).getItemMeta().getDisplayName().equals(exName)) {
				playerInv.setItem(i, new ItemStack(Material.AIR));
			}
		}
	}
	
	/**
	 * Donne le beacon a un joueur
	 * 
	 * @param p Le joueur qui doit reçevoir le beacon
	 */
	private static void giveBeacon(Player p) {
		ItemStack banner = new ItemStack(Material.BEACON, 1, (byte) 15);
		ItemMeta bannerM = banner.getItemMeta();
		bannerM.setDisplayName(getFormattedScenariosItemName());
		banner.setItemMeta(bannerM);
		p.getInventory().setItem(4, banner);
	}
	
	public static String getFormattedScenariosItemName() {
		return "§e"+LanguageBuilder.getContent("ITEM", "configuration", InventoryRegister.language.getSelectedLanguage(), true);
	}
}
