package fr.thedarven.configuration.builders.childs;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;

public class NoNether extends OptionBoolean {
	
	public NoNether(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue);
	}
	
	public NoNether(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pValue);
	}
	
	/**
	 * D�sactive la t�l�portation dans le nether
	 * 
	 * @param e L'�v�nement de t�l�portation par un portail
	 */
	@EventHandler
	public void onPortalTeleport(PlayerPortalEvent e){
		if(this.value) {
			e.setCancelled(true);
		}
	}
}