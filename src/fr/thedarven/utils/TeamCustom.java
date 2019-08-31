package fr.thedarven.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

public class TeamCustom {

	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	public static Scoreboard board = manager.getNewScoreboard();
	public static Objective objective = board.registerNewObjective("health", "health");
	
	private static List<TeamCustom> listTeam = new ArrayList<>();
	public static int maxPlayer = 9;
	
	private Team team;
	private int taupeTeam;
	private int superTaupeTeam;
	private boolean spectator;
	private List<UUID> listPlayer;
	private boolean alive;
	
	public TeamCustom(String name, int pColor, int pTaupe, int pSuperTaupe, boolean pSpectator, boolean pAlive) {
		team = board.registerNewTeam(name);
		team.setPrefix("§"+CodeColor.codeColorBP(pColor));
		team.setSuffix("§f");
		
		taupeTeam = pTaupe;
		superTaupeTeam = pSuperTaupe;
		spectator = pSpectator;
		listPlayer = new ArrayList<>();
		alive = pAlive;
		
		InventoryTeamsElement inv = new InventoryTeamsElement(name, CodeColor.codeColorPB(CodeColor.codeColorBP(pColor)));
		new InventoryParametres(inv);
		new InventoryPlayers(inv);
		new InventoryDeleteTeams(inv);
		
		listTeam.add(this);
	}
	
	public TeamCustom(String name, String pColor, int pTaupe, int pSuperTaupe, boolean pSpectator, boolean pAlive) {
		team = board.registerNewTeam(name);
		if(name.startsWith("Taupes") || name.startsWith("SuperTaupe"))
			team.setPrefix("§"+pColor+"["+name+"] ");
		else
			team.setPrefix("§"+pColor);
		team.setSuffix("§f");
		
		taupeTeam = pTaupe;
		superTaupeTeam = pSuperTaupe;
		spectator = pSpectator;
		listPlayer = new ArrayList<>();
		alive = pAlive;
		
		InventoryTeamsElement inv = new InventoryTeamsElement(name, CodeColor.codeColorPB(pColor));
		new InventoryParametres(inv);
		new InventoryPlayers(inv);
		new InventoryDeleteTeams(inv);
		
		listTeam.add(this);
	}
	
	public Team getTeam() {
		return team;
	}
	
	public boolean isTaupeTeam() {
		return taupeTeam != 0;
	}
	
	public boolean isSuperTaupeTeam() {
		return superTaupeTeam != 0;
	}
	
	public int getTaupeTeamNumber() {
		return taupeTeam;
	}
	
	public int getSuperTaupeTeamNumber() {
		return superTaupeTeam;
	}
	
	public boolean getSpectator() {
		return spectator;
	}
	
	public List<UUID> getListPlayer(){
		return listPlayer;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean pAlive) {
		alive = pAlive;
	}
	
	public void deleteTeam() {
		for(PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
			if(pl.getTeam() == this)
				pl.setTeam(null);
			if(pl.getStartTeam() == this)
				pl.setTeam(null);
			if(pl.getTaupeTeam() == this)
				pl.setTaupeTeam(null);
			if(pl.getSuperTaupeTeam() == this)
				pl.setSuperTaupeTeam(null);
		}
		InventoryTeamsElement.removeTeam(team.getName());
		team.unregister();
		listTeam.remove(this);
	}
	
	@SuppressWarnings("deprecation")
	public void joinTeam(UUID uuid) {
		if((alive && team.getEntries().size() < maxPlayer) || this.spectator) {
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(uuid);
			Player p = Bukkit.getPlayer(uuid);
			
			if(pl.getTeam() != null)
				pl.getTeam().leaveTeam(pl.getUuid());
			
			team.addEntry(pl.getCustomName());	
			listPlayer.add(uuid);	
			objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			if(p != null) {
				Score score = objective.getScore(p);
				score.setScore(20);
				p.setScoreboard(board);
			}
			
			pl.setTeam(this);
			if(TaupeGun.etat.equals(EnumGame.LOBBY))
				pl.setStartTeam(this);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void joinTeam(String pseudo) {
		if((alive && team.getEntries().size() < maxPlayer) || this.spectator) {
			Player p = Bukkit.getPlayer(pseudo);
			
			team.addEntry(pseudo);	
			listPlayer.add(p.getUniqueId());	
			objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			if(p != null) {
				Score score = objective.getScore(p);
				score.setScore(20);
				p.setScoreboard(board);
			}
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void leaveTeam(UUID uuid){
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(uuid);
		Player p = Bukkit.getPlayer(uuid);
		
		team.removeEntry(pl.getCustomName());
		listPlayer.remove(uuid);
		if(p != null)
			board.resetScores(p);
		pl.setTeam(null);
	}
	
	public static List<TeamCustom> getAllTeams() {
		return listTeam;
	}
	
	public static List<TeamCustom> getAllAliveTeams() {
		List<TeamCustom> list = new ArrayList<>();
		for(TeamCustom team : listTeam){
			if(!team.getSpectator() && team.isAlive())
				list.add(team);
		}
		return list;
	}
	
	public static List<TeamCustom> getAllStartAliveTeams() {
		List<TeamCustom> list = new ArrayList<>();
		for(TeamCustom team : listTeam){
			if(!team.getSpectator() && !team.isTaupeTeam() && !team.isSuperTaupeTeam() && team.isAlive())
				list.add(team);
		}
		return list;
	}
	
	public static TeamCustom getTeamCustom(String pName) {
		for(TeamCustom team : listTeam){
			if(team.getTeam().getName().equals(pName))
				return team;
		}
		return null;
	}
	
	public static TeamCustom getSpectatorTeam() {
		for(TeamCustom team : listTeam){
			if(team.getSpectator())
				return team;
		}
		return null;
	}
	
	public static List<TeamCustom> getTaupeTeams() {
		List<TeamCustom> list = new ArrayList<>();
		for(TeamCustom team : listTeam){
			if(team.isTaupeTeam())
				list.add(team);
		}
		return list;
	}
	
	public static List<TeamCustom> getSuperTaupeTeams() {
		List<TeamCustom> list = new ArrayList<>();
		for(TeamCustom team : listTeam){
			if(team.isSuperTaupeTeam())
				list.add(team);
		}
		return list;
	}
	
	public static void deleteTeamTaupe() {
		for(TeamCustom teamParcours : listTeam){
			if(teamParcours.isTaupeTeam() || teamParcours.isSuperTaupeTeam()) {
				teamParcours.deleteTeam();
			}
		}
	}
}
