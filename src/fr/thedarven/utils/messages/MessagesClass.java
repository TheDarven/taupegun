package fr.thedarven.utils.messages;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class MessagesClass {
	
	public static void CannotCommandOperatorMessage(Player p) {
		String operatorMessage = "§a"+"[TaupeGun]§c "+LanguageBuilder.getContent("COMMAND", "operator", InventoryRegister.language.getSelectedLanguage(), true);
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
						String teamMessage = "§e"+LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", InventoryRegister.language.getSelectedLanguage(), true)+"§7"+p.getName()+": "+messageCommand;
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
					String teamMessage = "§e"+LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", InventoryRegister.language.getSelectedLanguage(), true)+"§7"+p.getName()+": "+messageCommand;
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
		String nameAlreadyUsedMessage = "§c"+LanguageBuilder.getContent("TEAM", "nameAlreadyUsed", InventoryRegister.language.getSelectedLanguage(), true);
		Title.sendActionBar(p, nameAlreadyUsedMessage);
	}
	
	public static void TeamChangeNameMessage(Player p, String team) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("teamName", "§e§l"+team+"§r§a");
		String teamRenamedMessage = TextInterpreter.textInterpretation("§a"+LanguageBuilder.getContent("TEAM", "teamRenamed", InventoryRegister.language.getSelectedLanguage(), true), params);
		
		Title.sendActionBar(p, teamRenamedMessage);
	}
	
	//GAME
	public static void TaupeAnnonceMessage(Player p) {
		String moleMessageInfo = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageInfo", InventoryRegister.language.getSelectedLanguage(), true);
		String moleMessageT = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageT", InventoryRegister.language.getSelectedLanguage(), true);
		String moleMessageReveal = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageReveal", InventoryRegister.language.getSelectedLanguage(), true);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("teamName", "§e§l"+PlayerTaupe.getPlayerManager(p.getUniqueId()).getClaimTaupe()+"§r§6");
		String moleMessageClaim = TextInterpreter.textInterpretation("§6"+LanguageBuilder.getContent("CONTENT", "moleMessageClaim", InventoryRegister.language.getSelectedLanguage(), true), params);
		
		p.sendMessage(ChatColor.RED+"---------------");
		p.sendMessage(moleMessageInfo);
		p.sendMessage(moleMessageT);
		p.sendMessage(moleMessageReveal);
		p.sendMessage(moleMessageClaim);
		p.sendMessage(ChatColor.RED+"---------------");
	}
	
	public static void SuperTaupeAnnonceMessage(Player p) {
		String superMoleMessageInfo = "§6"+LanguageBuilder.getContent("CONTENT", "superMoleMessageInfo", InventoryRegister.language.getSelectedLanguage(), true);
		String superMoleMessageT = "§6"+LanguageBuilder.getContent("CONTENT", "superMoleMessageT", InventoryRegister.language.getSelectedLanguage(), true);
		String superMoleMessageReveal = "§6"+LanguageBuilder.getContent("CONTENT", "superMoleMessageReveal", InventoryRegister.language.getSelectedLanguage(), true);
		
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
		String killListMessage = "§l§6"+LanguageBuilder.getContent("CONTENT", "killList", InventoryRegister.language.getSelectedLanguage(), true);
		Bukkit.broadcastMessage(killListMessage);
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
			if(pc.getKill() != 0) {
				Bukkit.broadcastMessage(ChatColor.BOLD+""+ChatColor.GREEN+pc.getName()+": "+ChatColor.RESET+" "+pc.getKill());
			}
		}
	}
	
	public static void TabMessage(Player p) {
		String authorMessage = LanguageBuilder.getContent("SCOREBOARD", "author", InventoryRegister.language.getSelectedLanguage(), true);
		Title.sendTabHF(p, "§6------------------\nTaupeGun\n------------------\n", "\n§2"+authorMessage+"\n§adiscord.gg/HZyS5T7");
	}
}
