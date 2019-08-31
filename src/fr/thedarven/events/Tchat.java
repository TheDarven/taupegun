package fr.thedarven.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.MessagesClass;

@SuppressWarnings("deprecation")
public class Tchat implements Listener {

	public Tchat(TaupeGun pl) {
	}
	
	@EventHandler
	public void writeTchat(PlayerChatEvent e){
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId());
			Player p = e.getPlayer();
			
			e.setCancelled(true);
			
			if(pl.isAlive()) {
				if(InventoryRegister.tchatequipe.getValue()) {
					if(e.getMessage().startsWith("!")) {
						String color = "";
						if(pl.getTeam() != null && pl.getTeam().getTeam() != null)
							color = pl.getTeam().getTeam().getPrefix();
						Bukkit.broadcastMessage(color+p.getName()+": §7"+e.getMessage().substring(1));
					}else {
						if(pl.isTaupe() && pl.getTeam() == pl.getTaupeTeam())
							MessagesClass.CommandTaupeMessageMessage(p, e.getMessage().split(" "), pl.getTeam());
						else if(pl.isSuperTaupe() && pl.getTaupeTeam() == pl.getSuperTaupeTeam())
							MessagesClass.CommandSupertaupeMessageMessage(p, e.getMessage().split(" "), pl.getTeam());
						else {
							for(PlayerTaupe pl1 : PlayerTaupe.getAllPlayerManager()) {
								if(pl1.getTeam() == pl.getTeam() && pl1.isOnline()) {
									pl1.getPlayer().sendMessage("§e[Equipe] §7"+p.getName()+": "+e.getMessage());
								}
							}	
						}
					}
				}else {
					String color = "";
					if(pl.getTeam() != null && pl.getTeam().getTeam() != null)
						color = pl.getTeam().getTeam().getPrefix();
					Bukkit.broadcastMessage(color+p.getName()+": §7"+e.getMessage());
				}
			}else {
				for(PlayerTaupe pl1 : PlayerTaupe.getAllPlayerManager()) {
					if(!pl1.isAlive() && pl1.isOnline())
						pl1.getPlayer().sendMessage("§7[Spec] "+e.getPlayer().getName()+": "+e.getMessage());
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
