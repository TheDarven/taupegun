package fr.thedarven.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class MessagesEventClass {
	//TEAMS
	public static void TeamDeletePlayerMessage(InventoryClickEvent e) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("playerName", "§e§l"+e.getCurrentItem().getItemMeta().getDisplayName()+"§f§r");
		String isRemovingMessage = TextInterpreter.textInterpretation("§f"+LanguageBuilder.getContent("TEAM", "isDeleting", InventoryRegister.language.getSelectedLanguage(), true), params);
		
		Title.sendActionBar((Player) e.getWhoClicked(), isRemovingMessage);
	}
	
	public static void TeamAddPlayerMessage(InventoryClickEvent e) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("playerName", "§e§l"+e.getCurrentItem().getItemMeta().getDisplayName()+"§a§r");
		String isAddingMessage = TextInterpreter.textInterpretation("§a"+LanguageBuilder.getContent("TEAM", "isAdding", InventoryRegister.language.getSelectedLanguage(), true), params);
		
		Title.sendActionBar((Player) e.getWhoClicked(), isAddingMessage);
	}
}
