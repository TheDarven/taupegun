package fr.thedarven.scenario.children;

import fr.thedarven.TaupeGun;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.builders.OptionBoolean;

public class NoEnderPearlDamage extends OptionBoolean {
	
	public NoEnderPearlDamage(TaupeGun main, InventoryGUI parent) {
		super(main, "No Enderpearl Damage", "Désactive les dégâts causés par les enderpearl.", "MENU_CONFIGURATION_SCENARIO_PEARLDAMAGE", Material.ENDER_PEARL, parent, false);
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
