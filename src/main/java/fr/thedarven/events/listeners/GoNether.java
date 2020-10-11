package fr.thedarven.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.configuration.builders.InventoryRegister;

public class GoNether implements Listener {

	private TaupeGun main;

	public GoNether(TaupeGun main) {
		this.main = main;
	}

	@EventHandler
	public void join(PlayerTeleportEvent e) {
		if(EnumGameState.isCurrentState(EnumGameState.GAME)){
			if(e.getCause().equals(TeleportCause.NETHER_PORTAL)){
				Player p = e.getPlayer();
				if(p.getWorld() == UtilsClass.getWorld()){
					PlayerTaupe.getPlayerManager(p.getUniqueId()).setNetherPortal(e.getTo());
				}
			}	
		}
	}
	
	@EventHandler
	public void onPortalTeleport(PlayerPortalEvent e){
		if(this.main.getGameManager().getTimer() > InventoryRegister.murtime.getValue() * 60 || e.getCause().equals(TeleportCause.END_PORTAL))
			e.setCancelled(true);
	}
}
