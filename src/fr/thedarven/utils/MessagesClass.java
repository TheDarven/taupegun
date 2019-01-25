package fr.thedarven.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.api.Title;

public class MessagesClass {
	
	public static void CannotCommandOperatorMessage(Player p) {
		p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Vous n'avez pas les permissions pour utiliser cette commande.");
	}
	
	//TAUPECOMMANDS
	public static void CannotCommandTaupelistMessage(Player p) {
		p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Les taupes ne sont pas encore annoncées.");
	}
	
	public static void Taupe1ListMessage(Player p) {
		String listTaupe = ChatColor.RED+""+ChatColor.BOLD+"Taupes 1: "+ChatColor.RESET+""+ChatColor.RED;
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()){
			if(pc.getTaupeTeam() == 1) {
				listTaupe = listTaupe +pc.getCustomName()+" ";
			}
		}
		p.getPlayer().sendMessage(listTaupe);
	}
	
	public static void Taupe2ListMessage(Player p) {
		String listTaupe = ChatColor.RED+""+ChatColor.BOLD+"Taupes 2: "+ChatColor.RESET+""+ChatColor.RED;
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()){
			if(pc.getTaupeTeam() == 2) {
				listTaupe = listTaupe +pc.getCustomName()+" ";
			}
		}
		p.getPlayer().sendMessage(listTaupe);
	}
	
	public static void SuperTaupeListMessage(Player p) {
		String listTaupe = ChatColor.DARK_RED+""+ChatColor.BOLD+"Super taupes: "+ChatColor.RESET+""+ChatColor.DARK_RED;
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()){
			if(pc.getSuperTaupeTeam() == 1) {
				listTaupe = listTaupe +pc.getCustomName()+" ";
			}
		}
		p.getPlayer().sendMessage(listTaupe);
	}

	//STARTITEM
	public static void InventorySaveMessage(Player p) {
		Title.sendActionBar(p, ChatColor.GREEN+" L'inventaire a été ajouté avec succès !");
	}
	
	//TEAMS	
	public static void CannotTeamCreateNameAlreadyMessage(Player p) {
		Title.sendActionBar(p, ChatColor.RED+"Ce nom d'équipe est déjà prit !");
	}
	
	public static void TeamChangeNameMessage(Player p, String team) {
		Title.sendActionBar(p, ChatColor.GREEN+" L'équipe a été renomé en "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.GREEN+" avec succès.");
	}
	
	public static void TeamDeleteMessage(Player p, String team) {
		Title.sendActionBar(p, ChatColor.WHITE+" L'équipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.WHITE+" a été supprimée avec succès.");
	}
	
	public static void TeamCannotAddPlayerMessage(Player p, String team) {
		Title.sendActionBar(p, ChatColor.RED+" L'équipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.RED+" est déjà complète.");
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
		String listTaupe = ChatColor.RED+""+ChatColor.BOLD+"Taupes 1: "+ChatColor.RESET+""+ChatColor.RED;
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()){
			if(pc.getTaupeTeam() == 1) {
				listTaupe = listTaupe+pc.getCustomName()+" ";
			}
		}
		Bukkit.getServer().broadcastMessage(listTaupe);	
		
		if(InventoryRegister.nombretaupes.getValue() == 2){
			listTaupe = ChatColor.RED+""+ChatColor.BOLD+"Taupes 2: "+ChatColor.RESET+""+ChatColor.RED;
			for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()){
				if(pc.getTaupeTeam() == 2) {
					listTaupe = listTaupe+pc.getCustomName()+" ";
				}
			}
			Bukkit.getServer().broadcastMessage(listTaupe);		
		}
	}
	
	public static void FinalSuperTaupeAnnonceMessage() {
		String listTaupe = ChatColor.DARK_RED+""+ChatColor.BOLD+"Super taupes: "+ChatColor.RESET+""+ChatColor.DARK_RED;
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()){
			if(pc.getSuperTaupeTeam() == 1) {
				listTaupe = listTaupe +pc.getCustomName()+" ";
			}
		}
		Bukkit.getServer().broadcastMessage(listTaupe);	
	}
	
	public static void FinalKillAnnonceMessage() {
		Bukkit.broadcastMessage(ChatColor.BOLD+""+ChatColor.GOLD+"======== Liste des kills ========");
		for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
			if(pc.getKill() != 0) {
				Bukkit.broadcastMessage(ChatColor.BOLD+""+ChatColor.GREEN+pc.getCustomName()+": "+ChatColor.RESET+" "+pc.getKill());
			}
		}
	}
}
