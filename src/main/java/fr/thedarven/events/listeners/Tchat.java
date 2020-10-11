package fr.thedarven.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;

@SuppressWarnings("deprecation")
public class Tchat implements Listener {

	public Tchat() {}
	
	@EventHandler
	public void writeTchat(PlayerChatEvent e){
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId());
		Player p = e.getPlayer();
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)){
			e.setCancelled(true);
			Bukkit.broadcastMessage("§7"+getTeamColor(pl)+p.getName()+": §r"+e.getMessage());	
		}else if(EnumGameState.isCurrentState(EnumGameState.GAME)){
			e.setCancelled(true);
			if(pl.isAlive()) {
				if(InventoryRegister.tchatequipe.getValue()) {
					if(e.getMessage().startsWith("!") || e.getMessage().startsWith("*")) 
						Bukkit.broadcastMessage(getTeamColor(pl)+p.getName()+": §7"+e.getMessage().substring(1));
					else {
						if(pl.isTaupe() && pl.getTeam() == pl.getTaupeTeam())
							MessagesClass.CommandTaupeMessageMessage(p, e.getMessage().split(" "), pl.getTeam());
						else if(pl.isSuperTaupe() && pl.getTaupeTeam() == pl.getSuperTaupeTeam())
							MessagesClass.CommandSupertaupeMessageMessage(p, e.getMessage().split(" "), pl.getTeam());
						else {
							String teamMessage = "§e"+LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", InventoryRegister.language.getSelectedLanguage(), true)+"§7"+p.getName()+": "+e.getMessage();
							for(PlayerTaupe pl1 : PlayerTaupe.getAllPlayerManager()) {
								if(pl1.getTeam() == pl.getTeam() && pl1.isOnline()) {
									pl1.getPlayer().sendMessage(teamMessage);
								}
							}	
						}
					}
				}else 
					Bukkit.broadcastMessage(getTeamColor(pl)+p.getName()+": §7"+e.getMessage());
			}else {
				String spectatorMessage = "§7"+LanguageBuilder.getContent("EVENT_TCHAT", "spectatorMessage", InventoryRegister.language.getSelectedLanguage(), true)+p.getName()+": "+e.getMessage();
				for(PlayerTaupe pl1 : PlayerTaupe.getAllPlayerManager()) {
					if(!pl1.isAlive() && pl1.isOnline())
						pl1.getPlayer().sendMessage(spectatorMessage);
				}
			}
		}
	}
	
	@EventHandler
	public void writeCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
		if(EnumGameState.isCurrentState(EnumGameState.GAME)) {
			if(e.getMessage().startsWith("/me") && !pl.isAlive()){
				e.setCancelled(true);
			}else if((e.getMessage().startsWith("/tell") || e.getMessage().startsWith("/msg")) && !pl.isAlive()) {
				e.setCancelled(true);
				String cannotPrivateMessage = "§a[TaupeGun]§c "+LanguageBuilder.getContent("EVENT_TCHAT", "cannotPrivateMessage", InventoryRegister.language.getSelectedLanguage(), true);
				p.sendMessage(cannotPrivateMessage);
			}
		}
	}
	
	private String getTeamColor(PlayerTaupe pl) {
		String color = "";
		if(pl.getTeam() != null && pl.getTeam().getTeam() != null)
			color = pl.getTeam().getTeam().getPrefix();
		return color;
	}

}
