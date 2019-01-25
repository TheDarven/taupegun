package fr.thedarven.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.PlayerTaupe;
import fr.thedarven.utils.api.Title;

public class MessagesClass {
	
	public static void CannotCommandOperatorMessage(Player p) {
		p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Vous n'avez pas les permissions pour utiliser cette commande.");
	}
	
	public static void CannotCommandStartAlreadyStartMessage(Player p) {
		p.sendMessage(ChatColor.RED+"La partie a d�j� commenc� !");
	}
	
	//LOGIN
	public static void StartStopMessage(Player p) {
		Title.title(p, ChatColor.RED +"Lancement annul�", "", 10);
	}
	
	//DEATH
	public static void DeathMessage(Player p) {
		p.sendMessage("�cVous �tes � pr�sent mort. Merci de vous muter ou de changer de channel mumble.");
		p.sendMessage("�cVous pouvez savoir la liste des taupes en faisant /taupelist");
	}
	
	//OTHERSCOMMANDS
	public static void CannotCommandReviveMessage(Player p) {
		p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Il est impossible de r�animer quelqu'un � ce stade de la partie.");
	}
	
	//TAUPECOMMANDS
	public static void CannotCommandTaupelistMessage(Player p) {
		p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Les taupes ne sont pas encore annonc�es.");
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
	
	//STARTCOMMAND
	public static void CannotCommandStartTeam1Message(Player p) {
		p.sendMessage(ChatColor.RED+"Il faut au minimum deux �quipes.");
	}
	
	public static void CannotCommandStartTeam2Message(Player p) {
		p.sendMessage(ChatColor.RED+"Il faut au minimum trois �quipes.");
	}
	
	public static void CannotCommandStartPlayerMessage(Player p) {
		p.sendMessage(ChatColor.RED+"Il n'y a pas assez de joueurs par �quipe.");
	}
	
	public static void CannotCommandStartOfflinePlayerMessage(Player p) {
		p.sendMessage(ChatColor.RED+"Les joueurs ne sont pas tous connect�s.");
	}
	
	public static void CommandStartStartMessage(Player p) {
		p.sendMessage(ChatColor.BLUE+"La partie peut commencer !");
	}
	
	//STARTITEM
	public static void InventorySaveMessage(Player p) {
		Title.sendActionBar(p, ChatColor.GREEN+" L'inventaire a �t� ajout� avec succ�s !");
	}
	
	//TEAMS
	public static void CannotTeamCreateNameBigMessage(Player p) {
		Title.sendActionBar(p, ChatColor.RED+"Le nom de l'�quipe ne doit pas d�passer 16 caract�res.");
	}
	
	public static void CannotTeamCreateNameAlreadyMessage(Player p) {
		Title.sendActionBar(p, ChatColor.RED+"Ce nom d'�quipe est d�j� prit !");
	}
	
	public static void TeamCreateMessage(Player p, String team) {
		Title.sendActionBar(p, ChatColor.GREEN+" L'�quipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.GREEN+" a �t� cr�e avec succ�s.");
	}
	
	public static void TeamChangeNameMessage(Player p, String team) {
		Title.sendActionBar(p, ChatColor.GREEN+" L'�quipe a �t� renom� en "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.GREEN+" avec succ�s.");
	}
	
	public static void TeamDeleteMessage(Player p, String team) {
		Title.sendActionBar(p, ChatColor.WHITE+" L'�quipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.WHITE+" a �t� supprim�e avec succ�s.");
	}
	
	public static void TeamCannotAddPlayerMessage(Player p, String team) {
		Title.sendActionBar(p, ChatColor.RED+" L'�quipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.RED+" est d�j� compl�te.");
	}
	
	//GAME
	public static void TaupeAnnonce5Message() {
		Bukkit.broadcastMessage(ChatColor.GREEN+"Annonce des taupes dans 5 secondes.");
	}
	
	public static void SuperTaupeAnnonce5Message() {
		Bukkit.broadcastMessage(ChatColor.GREEN+"Annonce des supertaupes dans 5 secondes.");
	}
	
	public static void MurAnnonce3Message() {
		Bukkit.broadcastMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.WHITE+" Le mur va commencer � se r�duire dans 3 minutes !");
	}
	
	public static void MurAnnonceNowMessage() {
		Bukkit.broadcastMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.WHITE+" Le mur commence � se r�duire !");
	}
		
	public static void TaupeAnnonceMessage(Player p) {
		p.sendMessage(ChatColor.RED+"---------------");
		p.sendMessage(ChatColor.GOLD+" Vous �tes une taupe, retournez votre �quipe et remportez la partie avec votre �quipe de taupes !");
		p.sendMessage(ChatColor.GOLD+" Tapez /t pour envoyer un message � votre �quipe.");
		p.sendMessage(ChatColor.GOLD+" Tapez /reveal pour vous r�v�ler aux yeux de tous et gagner une pomme d'or.");
		p.sendMessage(ChatColor.GOLD+" Tapez /claim pour re�evoir votre kit "+ChatColor.YELLOW+ChatColor.BOLD+PlayerTaupe.getPlayerManager(p.getUniqueId()).getClaimTaupe()+ChatColor.RESET+ChatColor.GOLD+". Attention, les items peuvent dropper au sol !");
		p.sendMessage(ChatColor.RED+"---------------");
	}
	
	public static void SuperTaupeAnnonceMessage(Player p) {
		p.sendMessage(ChatColor.RED+"---------------");
		p.sendMessage(ChatColor.GOLD+" Vous �tes une super taupe, retournez votre �quipe de taupe et remportez la partie avec votre �quipe de taupes !");
		p.sendMessage(ChatColor.GOLD+" Tapez /supert pour envoyer un message � votre �quipe.");
		p.sendMessage(ChatColor.GOLD+" Tapez /superreveal pour vous r�v�ler aux yeux de tous et gagner une pomme d'or.");
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
	
	//TEAMDELETE
	public static void TeamEliminateMessage(String team) {
		Bukkit.broadcastMessage(ChatColor.RED+"L'�quipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.RED+" a �t� �limin�e");
	}
	
	public static void TeamVictoryMessage(String team) {
		Bukkit.broadcastMessage(ChatColor.GREEN+"L'�quipe "+ChatColor.GOLD+team+ChatColor.GREEN+" a gagn� !");
	}
}
