package fr.thedarven.events.listener.stats;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import fr.thedarven.model.enums.EnumGameState;
import fr.thedarven.player.model.StatsPlayerTaupe;

public class ThrewArrowListener implements Listener {
	
	public ThrewArrowListener() {}

	@EventHandler
	public void onShootBowEvent(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();
		if (!EnumGameState.isCurrentState(EnumGameState.GAME) || player.getGameMode() != GameMode.SURVIVAL)
			return;

		StatsPlayerTaupe ps = StatsPlayerTaupe.getPlayerManager(player.getUniqueId());
		if (ps.isAlive()) {
			ps.addBowForceCounter(e.getForce());
			ps.addThrowedArrow(1);
		}
	}
}
