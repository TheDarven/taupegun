package fr.thedarven.events.listeners.stats;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import fr.thedarven.models.EnumGameState;
import fr.thedarven.models.PlayerTaupe;

public class RegeneratesHealthListener implements Listener {
	
	public RegeneratesHealthListener() {}
	
	@EventHandler
	public void regeneratesHealth(EntityRegainHealthEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();
		if (!EnumGameState.isCurrentState(EnumGameState.GAME) || player.getGameMode() != GameMode.SURVIVAL)
			return;

		PlayerTaupe ps = PlayerTaupe.getPlayerManager(player.getUniqueId());
		if (ps.isAlive()) {
			ps.addHealedLife(e.getAmount());
		}
	}
	
}
