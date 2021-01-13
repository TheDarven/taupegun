package fr.thedarven.events.listeners;

import fr.thedarven.models.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void breakBlock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT) && player.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		} else if (EnumGameState.isCurrentState(EnumGameState.GAME) && player.getGameMode() == GameMode.SURVIVAL) {
			PlayerTaupe ps = PlayerTaupe.getPlayerManager(player.getUniqueId());
			if (ps.isAlive()) {
				switch(e.getBlock().getType()) {
					case DIAMOND_ORE:
						ps.addMinedDiamond(1);
						break;
					case IRON_ORE:
						ps.addMinedIron(1);
						break;
					case GOLD_ORE:
						ps.addMinedGold(1);
						break;
					default:
						break;
				}
			}
		}
	}

}
