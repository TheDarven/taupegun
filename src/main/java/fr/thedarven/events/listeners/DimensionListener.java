package fr.thedarven.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.players.PlayerTaupe;

public class DimensionListener implements Listener {

	private TaupeGun main;

	public DimensionListener(TaupeGun main) {
		this.main = main;
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
			if (e.getCause().equals(TeleportCause.NETHER_PORTAL)) {
				Player p = e.getPlayer();
				if (p.getWorld() == this.main.getWorldManager().getWorld()) {
					PlayerTaupe.getPlayerManager(p.getUniqueId()).setNetherPortal(e.getTo());
				}
			}	
		}
	}
	
	@EventHandler
	public void onPortalTeleport(PlayerPortalEvent e) {
		if (this.main.getGameManager().getTimer() > this.main.getScenariosManager().wallShrinkingTime.getValue() || e.getCause() == TeleportCause.END_PORTAL) {
			e.setCancelled(true);
		}
	}
}
