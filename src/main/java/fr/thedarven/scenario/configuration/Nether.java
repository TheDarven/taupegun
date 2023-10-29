package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.builder.OptionBoolean;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

public class Nether extends OptionBoolean {
	
	public Nether(TaupeGun main, CustomInventory parent) {
		super(main, "Nether", "Active ou non le nether.", "MENU_CONFIGURATION_SCENARIO_NETHER", Material.OBSIDIAN, parent, true);
	}
	
	/**
	 * Désactive la téléportation dans le nether
	 * 
	 * @param e L'évènement de téléportation par un portail
	 */
	@EventHandler
	public void onPortalTeleport(PlayerPortalEvent e){
		if (!this.value) {
			e.setCancelled(true);
		}
	}
}
