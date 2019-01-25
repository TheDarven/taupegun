package fr.thedarven.configuration.temp;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;

public class NoEnderPearlDamage extends OptionBoolean {

	public NoEnderPearlDamage(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pPosition, pValue);
	}
	
	public NoEnderPearlDamage(String pName, String pDescription, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pValue);
	}
	
	@EventHandler
	public void onEnderPearl(PlayerTeleportEvent e){
		if(e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && this.value){
			e.setCancelled(true);
			e.getPlayer().teleport(e.getTo());
		}
	}
}
