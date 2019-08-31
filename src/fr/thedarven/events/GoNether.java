package fr.thedarven.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.configuration.builders.InventoryRegister;

public class GoNether implements Listener {

	public GoNether(TaupeGun pl) {
	}

	@EventHandler
	public void join(PlayerTeleportEvent e) {
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			if(e.getCause().equals(TeleportCause.NETHER_PORTAL)){
				Player p = e.getPlayer();
				if(p.getWorld().getName().equals("world")){
					PlayerTaupe.getPlayerManager(p.getUniqueId()).setNetherPortal(e.getTo());
				}
			}	
		}
	}
	
	@EventHandler
	public void onPortalTeleport(PlayerPortalEvent e){
		if(TaupeGun.timer > InventoryRegister.murtime.getValue()*60 || e.getCause().equals(TeleportCause.END_PORTAL)) {
			e.setCancelled(true);
		}
	}
}
