package fr.thedarven.events.stats;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;

public class ThrowedArrow implements Listener{
	
	public ThrowedArrow() {}

	@EventHandler
	public void onShootBowEvent(EntityShootBowEvent e) {
		if(e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if(EnumGameState.isCurrentState(EnumGameState.GAME) && player.getGameMode().equals(GameMode.SURVIVAL)) {
				PlayerTaupe ps = PlayerTaupe.getPlayerManager(player.getUniqueId());
				if(ps.isAlive()) {
					ps.addBowForceCounter(e.getForce());
					ps.addThrowedArrow(1);
				}
			}
		}
	}
}
