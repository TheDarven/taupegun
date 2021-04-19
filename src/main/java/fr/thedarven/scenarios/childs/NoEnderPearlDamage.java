package fr.thedarven.scenarios.childs;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;

public class NoEnderPearlDamage extends OptionBoolean {
	
	public NoEnderPearlDamage(InventoryGUI parent) {
		super("No Enderpearl Damage", "Désactive les dégâts causés par les enderpearl.", "MENU_CONFIGURATION_SCENARIO_PEARLDAMAGE", Material.ENDER_PEARL, parent, false);
	}
	
	/**
	 * Désactive les dégâts des enderpearl
	 * 
	 * @param e L'évènement de téléportation
	 */
	@EventHandler
	final public void onEnderPearl(PlayerTeleportEvent e){
		if (!this.value)
			return;

		if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL){
			e.setCancelled(true);
			e.getPlayer().teleport(e.getTo());
		}
	}
}
