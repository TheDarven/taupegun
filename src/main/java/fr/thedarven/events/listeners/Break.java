package fr.thedarven.events.listeners;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;

public class Break implements Listener {

	public Break() {}
	
	@SuppressWarnings("incomplete-switch")
	@EventHandler(priority = EventPriority.LOWEST)
	public void breakBlock(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT) && !p.getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
		}else if(EnumGameState.isCurrentState(EnumGameState.GAME) && p.getGameMode().equals(GameMode.SURVIVAL)) {
			PlayerTaupe ps = PlayerTaupe.getPlayerManager(p.getUniqueId());
			if(ps.isAlive()) {
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
				}
			}
		}
	}

}
