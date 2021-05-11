package fr.thedarven.utils.messages;

import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessagesClass {
	
	//TAUPECOMMANDS
	public static void CommandTaupeMessageMessage(Player p, String[] args, TeamCustom taupeTeam) {
		String messageCommand = "";
		for(int messageSize = 0; messageSize < args.length; messageSize++){
			messageCommand = messageCommand+" "+args[messageSize];
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			PlayerTaupe pc = PlayerTaupe.getPlayerManager(player.getUniqueId());
			if(pc.getTaupeTeam() == taupeTeam && pc.isAlive() && !pc.isSuperReveal()){
				if(!PlayerTaupe.getPlayerManager(p.getUniqueId()).isSuperReveal()) {
					if(pc.isReveal()) {
						// String teamMessage = "§l§7"+LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", InventoryRegister.language.getSelectedLanguage(), true)+"⋙ §r§f"+p.getName()+": "+messageCommand;
						String teamMessage = "§e"+LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", true)+"§7"+p.getName()+": "+messageCommand;
						player.sendMessage(teamMessage);
					}
					else
						player.sendMessage("§c"+p.getName()+":"+messageCommand);
				}
			}
		}

		TeamCustom spectatorTeam = TeamCustom.getSpectatorTeam();
		if(spectatorTeam != null) {
			for(String player : spectatorTeam.getTeam().getEntries()){
				if(Bukkit.getPlayer(player) != null)
					Bukkit.getPlayer(player).sendMessage("§c["+taupeTeam.getTeam().getName()+"] "+p.getName()+":"+messageCommand);
			}
		}
	}
	

	public static void CommandSupertaupeMessageMessage(Player p, String[] args, TeamCustom taupeTeam) {
		String messageCommand = "";
		for(int messageSize = 0; messageSize < args.length; messageSize++){
			messageCommand = messageCommand+" "+args[messageSize];
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			PlayerTaupe pc = PlayerTaupe.getPlayerManager(player.getUniqueId());
			if(pc.getSuperTaupeTeam() == taupeTeam && pc.isAlive()) {
				if(pc.isSuperReveal()) {
					String teamMessage = "§e"+LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", true)+"§7"+p.getName()+": "+messageCommand;
					player.sendMessage(teamMessage);
				}
				else
					player.sendMessage("§4"+p.getName()+":"+messageCommand);
			}
		}
		
		TeamCustom spectatorTeam = TeamCustom.getSpectatorTeam();
		if(spectatorTeam != null) {
			for(String player : spectatorTeam.getTeam().getEntries()){
				if(Bukkit.getPlayer(player) != null)
					Bukkit.getPlayer(player).sendMessage(ChatColor.DARK_RED+"["+taupeTeam.getTeam().getName()+"] "+p.getName()+":"+messageCommand);
			}
		}
	}

}
