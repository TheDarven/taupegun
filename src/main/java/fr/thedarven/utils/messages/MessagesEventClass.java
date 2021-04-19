package fr.thedarven.utils.messages;

import java.util.HashMap;
import java.util.Map;

import fr.thedarven.models.PlayerTaupe;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;

public class MessagesEventClass {
	//TEAMS
	public static void TeamDeletePlayerMessage(InventoryClickEvent e) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("playerName", "§e§l"+e.getCurrentItem().getItemMeta().getDisplayName()+"§f§r");
		String isRemovingMessage = TextInterpreter.textInterpretation("§f"+LanguageBuilder.getContent("TEAM", "isDeleting", true), params);
		
		Title.sendActionBar((Player) e.getWhoClicked(), isRemovingMessage);
	}
	
	public static void TeamAddPlayerMessage(Player sender, PlayerTaupe addedPlayer) {
		Map<String, String> params = new HashMap<>();
		params.put("playerName", "§e§l" + addedPlayer.getName() + "§a§r");
		String isAddingMessage = TextInterpreter.textInterpretation("§a" + LanguageBuilder.getContent("TEAM", "isAdding", true), params);
		
		Title.sendActionBar(sender, isAddingMessage);
	}
}
