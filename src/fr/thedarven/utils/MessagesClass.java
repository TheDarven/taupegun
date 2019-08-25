package fr.thedarven.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.api.Title;

public class MessagesClass {
	
	public static void CannotCommandOperatorMessage(Player p) {
		p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Vous n'avez pas les permissions pour utiliser cette commande.");
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
					if(pc.isReveal())
						player.sendMessage("§e[Equipe] §7"+p.getName()+": "+messageCommand);
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
					listTaupe = listTaupe +pc.getCustomName()+" ";
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
				if(pc.isSuperReveal())
					player.sendMessage("§e[Equipe] §7"+p.getName()+": "+messageCommand);
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
					listTaupe = listTaupe +pc.getCustomName()+" ";
			}
			p.getPlayer().sendMessage(listTaupe);
		}
	}
	
	//TEAMS	
	public static void CannotTeamCreateNameAlreadyMessage(Player p) {
		Title.sendActionBar(p, ChatColor.RED+"Ce nom d'équipe est déjà prit !");
	}
	
	public static void TeamChangeNameMessage(Player p, String team) {
		Title.sendActionBar(p, ChatColor.GREEN+" L'équipe a été renomé en "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.GREEN+" avec succès.");
	}
	
	//GAME
	public static void TaupeAnnonceMessage(Player p) {
		p.sendMessage(ChatColor.RED+"---------------");
		p.sendMessage(ChatColor.GOLD+" Vous êtes une taupe, retournez votre équipe et remportez la partie avec votre équipe de taupes !");
		p.sendMessage(ChatColor.GOLD+" Tapez /t pour envoyer un message à votre équipe.");
		p.sendMessage(ChatColor.GOLD+" Tapez /reveal pour vous révèler aux yeux de tous et gagner une pomme d'or.");
		p.sendMessage(ChatColor.GOLD+" Tapez /claim pour reçevoir votre kit "+ChatColor.YELLOW+ChatColor.BOLD+PlayerTaupe.getPlayerManager(p.getUniqueId()).getClaimTaupe()+ChatColor.RESET+ChatColor.GOLD+". Attention, les items peuvent dropper au sol !");
		p.sendMessage(ChatColor.RED+"---------------");
	}
	
	public static void SuperTaupeAnnonceMessage(Player p) {
		p.sendMessage(ChatColor.RED+"---------------");
		p.sendMessage(ChatColor.GOLD+" Vous êtes une super taupe, retournez votre équipe de taupe et remportez la partie avec votre équipe de taupes !");
		p.sendMessage(ChatColor.GOLD+" Tapez /supert pour envoyer un message à votre équipe.");
		p.sendMessage(ChatColor.GOLD+" Tapez /superreveal pour vous révèler aux yeux de tous et gagner une pomme d'or.");
		p.sendMessage(ChatColor.RED+"---------------");
	}
	
	//FIREWORKWIN
	public static void FinalTaupeAnnonceMessage() {
		for(TeamCustom team : TeamCustom.getTaupeTeams()) {
			String listTaupe = ChatColor.RED+""+ChatColor.BOLD+team.getTeam().getName()+": "+ChatColor.RESET+""+ChatColor.RED;
			for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
				if(pc.getTaupeTeam() == team)
					listTaupe = listTaupe +pc.getCustomName()+" ";
			}
			Bukkit.getServer().broadcastMessage(listTaupe);
		}
	}
	
	public static void FinalSuperTaupeAnnonceMessage() {
		for(TeamCustom team : TeamCustom.getSuperTaupeTeams()) {
			String listTaupe = ChatColor.DARK_RED+""+ChatColor.BOLD+team.getTeam().getName()+": "+ChatColor.RESET+""+ChatColor.DARK_RED;
			for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
				if(pc.getSuperTaupeTeam() == team)
					listTaupe = listTaupe +pc.getCustomName()+" ";
			}
			Bukkit.getServer().broadcastMessage(listTaupe);	
		}
	}
	
	public static void FinalKillAnnonceMessage() {
		Bukkit.broadcastMessage(ChatColor.BOLD+""+ChatColor.GOLD+"======== Liste des kills ========");
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
			if(pc.getKill() != 0) {
				Bukkit.broadcastMessage(ChatColor.BOLD+""+ChatColor.GREEN+pc.getCustomName()+": "+ChatColor.RESET+" "+pc.getKill());
			}
		}
	}
	
	public static void JoinTabMessage(Player p) {
		Title.sendTabHF(p, "§6------------------\nTaupeGun\n------------------\n", "\n§2Plugin par TheDarven\n§adiscord.gg/HZyS5T7");
	}
}
