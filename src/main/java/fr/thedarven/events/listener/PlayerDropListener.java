package fr.thedarven.events.listener;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.thedarven.game.model.enums.EnumGameState;

public class PlayerDropListener implements Listener {

	private static final Material[] noDropMaterials = {Material.BANNER, Material.BEACON, Material.PAPER};
	
	public PlayerDropListener() {}
	
	@EventHandler
	public void onItemDrop (PlayerDropItemEvent e) {
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT) && Arrays.asList(noDropMaterials).contains(e.getItemDrop().getItemStack().getType())) {
			e.setCancelled(true);
		}
	}
}
