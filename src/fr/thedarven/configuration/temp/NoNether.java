package fr.thedarven.configuration.temp;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;

public class NoNether extends OptionBoolean {
	
	public NoNether(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pPosition, pValue);
	}
	
	public NoNether(String pName, String pDescription, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pValue);
	}
	
	@EventHandler
	public void onPortalTeleport(PlayerPortalEvent e){
		if(this.value) {
			e.setCancelled(true);
		}
	}
}
