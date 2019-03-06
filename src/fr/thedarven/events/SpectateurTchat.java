package fr.thedarven.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

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
						pl1.getPlayer().sendMessage("§7[Spec] "+e.getPlayer().getName()+": "+e.getMessage());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void writeCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
		if(TaupeGun.etat.equals(EnumGame.GAME)) {
			if(e.getMessage().startsWith("/me") && !pl.isAlive()){
				e.setCancelled(true);
			}else if((e.getMessage().startsWith("/tell") || e.getMessage().startsWith("/msg")) && !pl.isAlive()) {
				e.setCancelled(true);
				p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Vous ne pouvez pas envoyer de messages privées.");
			}
		}
	}

}
