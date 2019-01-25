package fr.thedarven.events;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.EnumGame;
import fr.thedarven.main.PlayerTaupe;

public class GoNether implements Listener {

	public GoNether(TaupeGun pl) {
	}

	@EventHandler
	public void join(PlayerTeleportEvent e) {
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			if(e.getCause().equals(TeleportCause.NETHER_PORTAL)){
				Player p = e.getPlayer();
		    	Location loc = p.getLocation();
				if(e.getPlayer().getWorld().getName().equals("world")){
					PlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId()).setNetherPortal(e.getTo());
					Login.boards.get(e.getPlayer()).setLine(8, "➋ Portail :§e 0");
				}else{
					int distance = (int) Math.sqrt(loc.getX() * loc.getX() + loc.getZ()* loc.getZ());
					Login.boards.get(p).setLine(8, "➋ Centre :§e "+ distance);
				}
			}	
		}
	}
}
