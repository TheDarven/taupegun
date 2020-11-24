package fr.thedarven.main.metier;

import java.util.*;
import java.util.stream.Collectors;

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
import fr.thedarven.utils.CodeColor;
import fr.thedarven.utils.teams.TeamUtils;

import javax.annotation.Nullable;

public class TeamCustom {

	public final static int MAX_PLAYER_PER_TEAM = 9;

	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	public static Scoreboard board = manager.getNewScoreboard();
	public static Objective objective = board.registerNewObjective("health", "health");
	
	private static Map<String, TeamCustom> teams = new HashMap<>();

	private Team team;
	private int taupeTeam;
	private int superTaupeTeam;
	private boolean spectator;
	private List<UUID> players;
	private boolean alive;
	
	public TeamCustom(String name, int pColor, int pTaupe, int pSuperTaupe, boolean pSpectator, boolean pAlive) {
		team = board.registerNewTeam(name);
		team.setPrefix("§"+CodeColor.codeColorBP(pColor));
		team.setSuffix("§f");
		
		taupeTeam = pTaupe;
		superTaupeTeam = pSuperTaupe;
		spectator = pSpectator;
		players = new ArrayList<>();
		alive = pAlive;
		
		InventoryTeamsElement inv = new InventoryTeamsElement(name, CodeColor.codeColorPB(CodeColor.codeColorBP(pColor)));
		new InventoryParametres(inv);
		new InventoryPlayers(inv);
		new InventoryDeleteTeams(inv);

		teams.put(name, this);
	}
	
	public TeamCustom(String name, String pColor, int pTaupe, int pSuperTaupe, boolean pSpectator, boolean pAlive) {
		team = board.registerNewTeam(name);
		if (name.startsWith(TeamUtils.getMoleTeamName()) || name.startsWith(TeamUtils.getSuperMoleTeamName()))
			team.setPrefix("§"+pColor+"["+name+"] ");
		else
			team.setPrefix("§"+pColor);
		team.setSuffix("§f");
		
		taupeTeam = pTaupe;
		superTaupeTeam = pSuperTaupe;
		spectator = pSpectator;
		players = new ArrayList<>();
		alive = pAlive;
		
		InventoryTeamsElement inv = new InventoryTeamsElement(name, CodeColor.codeColorPB(pColor));
		new InventoryParametres(inv);
		new InventoryPlayers(inv);
		new InventoryDeleteTeams(inv);
		
		teams.put(name, this);
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
	
	public boolean isSpectator() {
		return spectator;
	}
	
	public List<UUID> getPlayers(){
		return players;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean pAlive) {
		alive = pAlive;
	}
	
	public void deleteTeam() {
		for (PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
			if (pl.getTeam() == this)
				pl.setTeam(null);
			if (pl.getStartTeam() == this)
				pl.setTeam(null);
			if (pl.getTaupeTeam() == this)
				pl.setTaupeTeam(null);
			if (pl.getSuperTaupeTeam() == this)
				pl.setSuperTaupeTeam(null);
		}
		InventoryTeamsElement.removeTeam(team.getName());
		teams.remove(this.team.getName());
		team.unregister();
	}
	
	@SuppressWarnings("deprecation")
	public void joinTeam(UUID uuid) {
		if ((!alive || team.getEntries().size() >= MAX_PLAYER_PER_TEAM) && !this.spectator)
			return;

		PlayerTaupe pl = PlayerTaupe.getPlayerManager(uuid);
		if (pl.getTeam() != null)
			pl.getTeam().leaveTeam(pl.getUuid());

		Player p = Bukkit.getPlayer(uuid);
		joinScoreboardTeam(pl.getName(), uuid, p);

		pl.setTeam(this);
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY))
			pl.setStartTeam(this);
	}

	@SuppressWarnings("deprecation")
	public void joinTeam(String pseudo) {
		if ((!alive || team.getEntries().size() >= MAX_PLAYER_PER_TEAM) && !this.spectator)
			return;

		Player p = Bukkit.getPlayer(pseudo);
		joinScoreboardTeam(pseudo, p.getUniqueId(), p);
	}

	private void joinScoreboardTeam(String name, UUID uuid, Player p) {
		team.addEntry(name);
		objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

		if (uuid != null)
			players.add(uuid);

		if (p != null) {
			Score score = objective.getScore(p);
			score.setScore(20);
			p.setScoreboard(board);
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void leaveTeam(UUID uuid){
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(uuid);
		Player p = Bukkit.getPlayer(uuid);
		
		team.removeEntry(pl.getName());
		players.remove(uuid);
		if (p != null)
			board.resetScores(p);
		pl.setTeam(null);
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY))
			pl.setStartTeam(null);
	}
	
	public static List<TeamCustom> getAllTeams() {
		return new ArrayList<>(teams.values());
	}
	
	public static List<TeamCustom> getAllAliveTeams() {
		return teams.values()
				.stream()
				.filter(team -> !team.isSpectator() && team.isAlive())
				.collect(Collectors.toList());
	}
	
	public static List<TeamCustom> getAllStartAliveTeams() {
		return teams.values()
				.stream()
				.filter(team -> !team.isSpectator() && !team.isTaupeTeam() && !team.isSuperTaupeTeam() && team.isAlive())
				.collect(Collectors.toList());
	}
	
	public static List<TeamCustom> getAllStartTeams() {
		return teams.values()
				.stream()
				.filter(team -> !team.isSpectator() && !team.isTaupeTeam() && !team.isSuperTaupeTeam())
				.collect(Collectors.toList());
	}
	
	public static TeamCustom getTeamCustom(String name) {
		return teams.get(name);
	}

	public static TeamCustom getSpectatorTeam() {
		return teams.values()
				.stream()
				.filter(TeamCustom::isSpectator)
				.findFirst()
				.orElse(null);
	}
	
	public static List<TeamCustom> getTaupeTeams() {
		return teams.values()
				.stream()
				.filter(TeamCustom::isTaupeTeam)
				.collect(Collectors.toList());
	}
	
	public static List<TeamCustom> getSuperTaupeTeams() {
		return teams.values()
				.stream()
				.filter(TeamCustom::isSuperTaupeTeam)
				.collect(Collectors.toList());
	}

	public static void deleteTeamTaupe() {
		List<TeamCustom> teamsValue = new ArrayList<>(teams.values());
		teamsValue.stream()
				.filter(team -> team.isTaupeTeam() || team.isSuperTaupeTeam())
				.forEach(TeamCustom::deleteTeam);
	}
	
	public static Optional<TeamCustom> getWinTeam() {
		if (TeamCustom.getAllAliveTeams().size() != 1)
			return Optional.empty();
		return Optional.ofNullable(getAllAliveTeams().get(0));
	}
}
