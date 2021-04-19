package fr.thedarven.utils.messages;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;

public class MessagesClass {
	
	public static void CannotCommandOperatorMessage(Player p) {
		String operatorMessage = "§c"+LanguageBuilder.getContent("COMMAND", "operator", true);
		p.sendMessage(operatorMessage);
	}
	
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
	
	public static void TaupeListMessage(Player p) {
		for(TeamCustom team : TeamCustom.getTaupeTeams()) {
			String listTaupe = "§c"+ChatColor.BOLD+team.getTeam().getName()+": "+ChatColor.RESET+"§c";
			for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
				if(pc.getTaupeTeam() == team)
					listTaupe = listTaupe +pc.getName()+" ";
			}
			p.getPlayer().sendMessage(listTaupe);
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
	
	public static void SuperTaupeListMessage(Player p) {
		for(TeamCustom team : TeamCustom.getSuperTaupeTeams()) {
			String listTaupe = ChatColor.DARK_RED+""+ChatColor.BOLD+team.getTeam().getName()+": "+ChatColor.RESET+""+ChatColor.DARK_RED;
			for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
				if(pc.getSuperTaupeTeam() == team)
					listTaupe = listTaupe +pc.getName()+" ";
			}
			p.getPlayer().sendMessage(listTaupe);
		}
	}
	
	//TEAMS	
	public static void CannotTeamCreateNameAlreadyMessage(Player p) {
		String nameAlreadyUsedMessage = "§c"+LanguageBuilder.getContent("TEAM", "nameAlreadyUsed", true);
		Title.sendActionBar(p, nameAlreadyUsedMessage);
	}
	
	public static void TeamChangeNameMessage(Player p, String team) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("teamName", "§e§l"+team+"§r§a");
		String teamRenamedMessage = TextInterpreter.textInterpretation("§a"+LanguageBuilder.getContent("TEAM", "teamRenamed", true), params);
		
		Title.sendActionBar(p, teamRenamedMessage);
	}
	
	//GAME
	public static void TaupeAnnonceMessage(Player p) {
		String moleMessageInfo = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageInfo", true);
		String moleMessageT = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageT", true);
		String moleMessageReveal = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageReveal", true);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("kitName", "§e§l"+PlayerTaupe.getPlayerManager(p.getUniqueId()).getClaimTaupe()+"§r§6");
		String moleMessageClaim = TextInterpreter.textInterpretation("§6"+LanguageBuilder.getContent("CONTENT", "moleMessageClaim", true), params);
		
		p.sendMessage(ChatColor.RED+"---------------");
		p.sendMessage(moleMessageInfo);
		p.sendMessage(moleMessageT);
		p.sendMessage(moleMessageReveal);
		p.sendMessage(moleMessageClaim);
		p.sendMessage(ChatColor.RED+"---------------");
	}
	
	public static void SuperTaupeAnnonceMessage(Player p) {
		String superMoleMessageInfo = "§6"+LanguageBuilder.getContent("CONTENT", "superMoleMessageInfo", true);
		String superMoleMessageT = "§6"+LanguageBuilder.getContent("CONTENT", "superMoleMessageT", true);
		String superMoleMessageReveal = "§6"+LanguageBuilder.getContent("CONTENT", "superMoleMessageReveal", true);
		
		p.sendMessage(ChatColor.RED+"---------------");
		p.sendMessage(superMoleMessageInfo);
		p.sendMessage(superMoleMessageT);
		p.sendMessage(superMoleMessageReveal);
		p.sendMessage(ChatColor.RED+"---------------");
	}
	
	//FIREWORKWIN
	public static void FinalTaupeAnnonceMessage() {
		for(TeamCustom team : TeamCustom.getTaupeTeams()) {
			String listTaupe = ChatColor.RED+""+ChatColor.BOLD+team.getTeam().getName()+": "+ChatColor.RESET+""+ChatColor.RED;
			for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
				if(pc.getTaupeTeam() == team)
					listTaupe = listTaupe +pc.getName()+" ";
			}
			Bukkit.getServer().broadcastMessage(listTaupe);
		}
	}
	
	public static void FinalSuperTaupeAnnonceMessage() {
		for(TeamCustom team : TeamCustom.getSuperTaupeTeams()) {
			String listTaupe = ChatColor.DARK_RED+""+ChatColor.BOLD+team.getTeam().getName()+": "+ChatColor.RESET+""+ChatColor.DARK_RED;
			for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
				if(pc.getSuperTaupeTeam() == team)
					listTaupe = listTaupe +pc.getName()+" ";
			}
			Bukkit.getServer().broadcastMessage(listTaupe);	
		}
	}
	
	public static void FinalKillAnnonceMessage() {
		String killListMessage = "§l§6"+LanguageBuilder.getContent("CONTENT", "killList", true);
		Bukkit.broadcastMessage(killListMessage);
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
			if(pc.getKill() != 0) {
				Bukkit.broadcastMessage(ChatColor.BOLD+""+ChatColor.GREEN+pc.getName()+": "+ChatColor.RESET+" "+pc.getKill());
			}
		}
	}
	
	public static void TabMessage(Player p) {
		String authorMessage = LanguageBuilder.getContent("SCOREBOARD", "author", true);
		Title.sendTabHF(p, "§6------------------\nTaupeGun\n------------------\n", "\n§2"+authorMessage+"\n§adiscord.gg/HZyS5T7");
	}
}
