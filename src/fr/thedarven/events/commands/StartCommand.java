package fr.thedarven.events.commands;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.Game;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.teams.TeamGraph;


public class StartCommand implements Listener {
Scoreboard board = TeamCustom.board;
public static TeamGraph graph;

	private TaupeGun plugin;

	public StartCommand(TaupeGun pl) {
		this.plugin = pl;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCommandes(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String msg = e.getMessage();
		String[] args = msg.split(" ");
			
		/* SI ON FAIT LA BONNE COMMANDE */
		if(args[0].equalsIgnoreCase("/start")){
			e.setCancelled(true);
			
			if(!p.isOp()){
				MessagesClass.CannotCommandOperatorMessage(p);
				return;
			}
			if(!EnumGameState.isCurrentState(EnumGameState.LOBBY)){
				p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "gameAlreadyStarted", InventoryRegister.language.getSelectedLanguage(), true));
				return;
			}
			
			Set<Team> teams = board.getTeams();
			if(teams.size() < 2 && !InventoryRegister.supertaupes.getValue()){
				p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "needTwoTeams", InventoryRegister.language.getSelectedLanguage(), true));
				return;
			}
			
			if(teams.size() < 3 && InventoryRegister.supertaupes.getValue()){
				p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "needThreeTeams", InventoryRegister.language.getSelectedLanguage(), true));
				return;
			}
			
			if(InventoryRegister.kits.getChilds().size() <= 1) {
				p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "notEnoughKits", InventoryRegister.language.getSelectedLanguage(), true));
				return;
			}
			
			for(Team team : teams){
				if((InventoryRegister.nombretaupes.getValue() == 1 && team.getEntries().size() == 2) || InventoryRegister.nombretaupes.getValue() == 2 && team.getEntries().size() < 4){
					p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "notEnoughPlayersPerTeam", InventoryRegister.language.getSelectedLanguage(), true));
					return;
				}
				for(String player : team.getEntries()){
					boolean online = false;
					for(Player login : Bukkit.getServer().getOnlinePlayers()){
						if(player.equals(login.getName())){
							online = true;
						}
					}
					if(online == false){
						p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "disconnectedPlayer", InventoryRegister.language.getSelectedLanguage(), true));
						return;
					}
				}
			}
			
			graph = new TeamGraph();
			
			Random r = new Random();
			for(Team team : teams){
				ArrayList<PlayerTaupe> listTaupes = new ArrayList<PlayerTaupe>();
				if(team.getSize() == 1 || team.getSize() == 3 || (team.getSize() > 3 && InventoryRegister.nombretaupes.getValue() == 1)) {
					ArrayList<PlayerTaupe> playerList = new ArrayList<>();
					for(OfflinePlayer player : team.getPlayers()){
						playerList.add(PlayerTaupe.getPlayerManager(player.getUniqueId()));
					}
					listTaupes.add(playerList.get(r.nextInt(team.getSize())));
					graph.addEquipes(listTaupes);
					
				}else if(team.getSize() > 3 && InventoryRegister.nombretaupes.getValue() == 2) {
					ArrayList<PlayerTaupe> playerList = new ArrayList<>();
					for(OfflinePlayer player : team.getPlayers()){
						playerList.add(PlayerTaupe.getPlayerManager(player.getUniqueId()));
					}
					int taupeInt1 = r.nextInt(team.getSize());
					int taupeInt2 = r.nextInt(team.getSize());
					while(taupeInt1 == taupeInt2)
						taupeInt2 = r.nextInt(team.getSize());
					listTaupes.add(playerList.get(taupeInt1));
					listTaupes.add(playerList.get(taupeInt2));
					graph.addEquipes(listTaupes);
				}
			}
			boolean resultat = graph.creationEquipes();
			if(!resultat) {
				p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "incorrectMoleNumber", InventoryRegister.language.getSelectedLanguage(), true));
				TeamCustom.deleteTeamTaupe();
				for(PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
					pl.setTaupeTeam(null);
					pl.setSuperTaupeTeam(null);
				}
				return;
			}
			
			p.sendMessage(ChatColor.BLUE+LanguageBuilder.getContent("START_COMMAND", "gameCanStart", InventoryRegister.language.getSelectedLanguage(), true));
			EnumGameState.setState(EnumGameState.WAIT);
			Bukkit.getScheduler().runTaskTimer(plugin, new Runnable(){
				@Override
				public void run(){
					if(TaupeGun.timerStart == 10){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.DARK_RED +"10", "", 10);
						}
					}
												
					if(TaupeGun.timerStart == 5){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.RED +"5", "", 10);
						}
					}
					if(TaupeGun.timerStart == 4){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.YELLOW +"4", "", 10);
						}
					}
					if(TaupeGun.timerStart == 3){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.GOLD +"3", "", 10);
						}
					}
					if(TaupeGun.timerStart == 2){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.GREEN +"2", "", 10);
						}
					}
					if(TaupeGun.timerStart == 1){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.DARK_GREEN +"1", "", 10);
						}
					}						
					if(TaupeGun.timerStart == 0){
						Bukkit.getScheduler().cancelAllTasks();
						Game.start();
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL , 1, 1);
							Title.title(player, ChatColor.DARK_GREEN +LanguageBuilder.getContent("START_COMMAND", "gameIsStarting", InventoryRegister.language.getSelectedLanguage(), true), "", 10);
						}
					}
					TaupeGun.timerStart--;
				}
			},20,20);		
		}	
	}
}