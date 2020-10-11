package fr.thedarven.events;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.thedarven.main.metier.EnumGameState;

public class Drop implements Listener {

	private static Material[] noDropMaterials = {Material.BANNER, Material.BEACON, Material.PAPER};
	
	public Drop() {}
	
	@EventHandler
	public void onItemDrop (PlayerDropItemEvent e) {
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT) && Arrays.asList(noDropMaterials).contains(e.getItemDrop().getItemStack().getType())) {
			e.setCancelled(true);
		}
	}
}
