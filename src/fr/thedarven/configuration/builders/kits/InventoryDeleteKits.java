package fr.thedarven.configuration.builders.kits;

import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryDelete;
import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.api.Title;
import net.md_5.bungee.api.ChatColor;

public class InventoryDeleteKits extends InventoryDelete {
	
	public InventoryDeleteKits(InventoryGUI pInventoryGUI) {
		super(pInventoryGUI, "Supprimer le kit", 9);
	}
	
	protected void deleteElement(Player p) {
		Title.sendActionBar(p, ChatColor.WHITE+" Le kit "+ChatColor.YELLOW+ChatColor.BOLD+getParent().getName()+ChatColor.RESET+ChatColor.WHITE+" a été supprimé avec succès.");
		InventoryKitsElement.removeKit(getParent().getName());
		p.openInventory(InventoryRegister.kits.getInventory());
	}
}
