package fr.thedarven.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;

@SuppressWarnings("deprecation")
public class SpectateurTchat implements Listener {

	public SpectateurTchat(TaupeGun pl) {
	}
	
	@EventHandler
	public void writeTchat(PlayerChatEvent e){
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId());
			if(!pl.isAlive()) {
				e.setCancelled(true);
				for(PlayerTaupe pl1 : PlayerTaupe.getAllPlayerManager()) {
					if(!pl1.isAlive() && pl1.isOnline()) {
						pl1.getPlayer().sendMessage("§7[Mort] "+e.getPlayer().getName()+": "+e.getMessage());
					}
				}
			}
		}
	}

}
