package fr.thedarven.events;

import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.teams.InventoryDeleteTeams;
import fr.thedarven.configuration.builders.teams.InventoryParametres;
import fr.thedarven.configuration.builders.teams.InventoryPlayers;
import fr.thedarven.configuration.builders.teams.InventoryTeamsElement;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.CodeColor;
import fr.thedarven.utils.ScoreboardModule;
import fr.thedarven.utils.api.ScoreboardSign;

public class Teams implements Listener {
	
public int a= -1;
public static ScoreboardManager manager = Bukkit.getScoreboardManager();
public static Scoreboard board = manager.getNewScoreboard();
static Objective objective = board.registerNewObjective("health", "health");


	public Teams(TaupeGun pl) {
	}
	@EventHandler
	public void join(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(board);
	}

	public static void newTeam(String name, int color){
		Team owner = board.registerNewTeam(name);
		
		owner.setPrefix("§"+CodeColor.codeColorBP(color));
		owner.setSuffix("§f");
		scoreboardPlayer();
		InventoryTeamsElement inv = new InventoryTeamsElement(name, CodeColor.codeColorPB(CodeColor.codeColorBP(color)));
		new InventoryParametres(inv);
		new InventoryPlayers(inv);
		new InventoryDeleteTeams(inv);
	}
	
	public static void newTeam(String name, String color){
		Team owner = board.registerNewTeam(name);
		
		if(name.startsWith("Taupes") || name.startsWith("SuperTaupe")) {
			owner.setPrefix("§"+color+"["+name+"] ");
		}else {
			owner.setPrefix("§"+color);
		}
		owner.setSuffix("§f");
		scoreboardPlayer();
		InventoryTeamsElement inv = new InventoryTeamsElement(name, CodeColor.codeColorPB(color));
		new InventoryParametres(inv);
		new InventoryPlayers(inv);
		new InventoryDeleteTeams(inv);
	}
	
	public static void deleteTeam(String name){
		Team owner = board.getTeam(name);
		owner.unregister();
		scoreboardPlayer();
		InventoryTeamsElement.removeTeam(name);
	}
	
	public static void joinTeam(String name, String p){
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(p.equals(player.getName())){
				
				objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
				@SuppressWarnings("deprecation")
				Score score = objective.getScore(player);
				score.setScore(20);
				
				player.setScoreboard(board);
				if(!TaupeGun.etat.equals(EnumGame.GAME) || PlayerTaupe.getPlayerManager(player.getUniqueId()).getTeamName().equals("aucune")) {
					PlayerTaupe.getPlayerManager(player.getUniqueId()).setTeamName(name);
				}
			}
		}
		Team owner = board.getTeam(name);
		owner.addEntry(p);
		scoreboardPlayer();
	}
	
	public static void joinInitTeam(String name, String p){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(p.equals(player.getName())){
				
				objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
				@SuppressWarnings("deprecation")
				Score score = objective.getScore(player);
				score.setScore(20);
				
				player.setScoreboard(board);
			}
		}
		Team owner = board.getTeam(name);
		owner.addEntry(p);
		scoreboardPlayer();
	}
	
	@SuppressWarnings("deprecation")
	public static void leaveTeam(String name, String p){
		Team owner = board.getTeam(name);
		owner.removeEntry(p);
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(p.equals(player.getName())){
				board.resetScores(player);
				if(!TaupeGun.etat.equals(EnumGame.GAME)) {
					PlayerTaupe.getPlayerManager(player.getUniqueId()).setTeamName("aucune");
				}
			}
		}
		scoreboardPlayer();
	}
	
	/* public static void renameTeam(String pNomAvant, String pNomApres) {
		Team owner = board.registerNewTeam(pNomApres);
		owner.setPrefix(board.getTeam(pNomAvant).getPrefix());
		owner.setSuffix(board.getTeam(pNomAvant).getSuffix());
		
		for(String p : board.getTeam(pNomAvant).getEntries()) {
			leaveTeam(pNomAvant, p);
			joinTeam(pNomApres, p);
		}
		board.getTeam(pNomAvant).unregister();;
		
		scoreboardPlayer();
	} */
	
	public static void scoreboardPlayer(){
		for(Entry<Player, ScoreboardSign> sign : ScoreboardModule.boards.entrySet()){
			ScoreboardModule.setJoueurs(sign.getKey());
		}
	}
}