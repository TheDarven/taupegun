package fr.thedarven.events;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;

public class Break implements Listener {

	public Break(TaupeGun pl) {
	}
	
	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT) && !p.getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
		}
	}

}
