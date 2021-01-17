package fr.thedarven.scenarios.childs;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

public class NoNether extends OptionBoolean {
	
	public NoNether(InventoryGUI parent) {
		super("No Nether", "Désactive l'accès au nether.", "MENU_CONFIGURATION_SCENARIO_NONETHER", Material.OBSIDIAN, parent, false);
	}
	
	/**
	 * Désactive la téléportation dans le nether
	 * 
	 * @param e L'évènement de téléportation par un portail
	 */
	@EventHandler
	public void onPortalTeleport(PlayerPortalEvent e){
		if (this.value) {
			e.setCancelled(true);
		}
	}
}
