package fr.thedarven.utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import fr.thedarven.utils.api.Title;
import net.md_5.bungee.api.ChatColor;

public class MessagesEventClass {
	
	
	//TAUPECOMMAND		
	
	//TEAMS
	public static void TeamDeletePlayerMessage(InventoryClickEvent e) {
		Title.sendActionBar((Player) e.getWhoClicked(), ChatColor.WHITE+" Le joueur "+ChatColor.YELLOW+ChatColor.BOLD+e.getCurrentItem().getItemMeta().getDisplayName()+ChatColor.RESET+ChatColor.WHITE+" a été supprimé de l'équipe.");
	}
	
	public static void TeamAddPlayerMessage(InventoryClickEvent e) {
		Title.sendActionBar((Player) e.getWhoClicked(), ChatColor.GREEN+" Le joueur "+ChatColor.YELLOW+ChatColor.BOLD+e.getCurrentItem().getItemMeta().getDisplayName()+ChatColor.RESET+ChatColor.GREEN+" a été ajouté à l'équipe.");
	}
}
