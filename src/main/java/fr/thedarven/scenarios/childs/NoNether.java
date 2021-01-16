package fr.thedarven.scenarios.childs;

import fr.thedarven.scenarios.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

import fr.thedarven.scenarios.OptionBoolean;

public class NoNether extends OptionBoolean {
	
	public NoNether(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue);
	}
	
	public NoNether(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pValue);
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
