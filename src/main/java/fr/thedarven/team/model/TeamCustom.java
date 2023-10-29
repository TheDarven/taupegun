package fr.thedarven.team.model;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.model.enums.EnumGameState;
import fr.thedarven.stats.model.StatsPlayer;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.teams.element.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.stream.Collectors;

public class TeamCustom {

	public final static int MAX_PLAYER_PER_TEAM = 9;

	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	public static Scoreboard board = manager.getNewScoreboard();
	public static Objective objective = board.registerNewObjective("health", "health");
	
	private static Map<String, TeamCustom> teams = new HashMap<>();

	private final TaupeGun main;

	private String name;
	private ColorEnum colorEnum;

	private Team team;
	private int taupeTeam;
	private int superTaupeTeam;
	private boolean spectator;
	private List<StatsPlayerTaupe> players;
	private boolean alive;
	
	public TeamCustom(TaupeGun main, String name, ColorEnum colorEnum, int pTaupe, int pSuperTaupe, boolean pSpectator, boolean pAlive) {
		this.main = main;
		team = board.registerNewTeam(name);
		if (name.startsWith(this.main.getTeamManager().getMoleTeamName()) || name.startsWith(this.main.getTeamManager().getSuperMoleTeamName())) {
			team.setPrefix(colorEnum.getColor() + "[" + name + "] ");
		} else {
			team.setPrefix(colorEnum.getColor());
		}
		team.setSuffix("§f");

		this.name = name;
		this.colorEnum = colorEnum;

		this.taupeTeam = pTaupe;
		this.superTaupeTeam = pSuperTaupe;
		this.spectator = pSpectator;
		this.players = new ArrayList<>();
		this.alive = pAlive;
		
		InventoryTeamsElement inv = new InventoryTeamsElement(this.main, name, colorEnum);
		InventoryTeamsParameters parameters = new InventoryTeamsParameters(this.main, inv);
		new InventoryTeamsChangeColor(this.main, parameters);
		new InventoryTeamsRename(this.main, parameters);
		new InventoryTeamsPlayers(this.main, inv);
		new InventoryDeleteTeams(this.main, inv);
		
		teams.put(name, this);
	}

	public ColorEnum getColorEnum() {
		return colorEnum;
	}

	public void setColorEnum(ColorEnum colorEnum) {
		this.colorEnum = colorEnum;
	}

	public Team getTeam() { return this.team; }
	
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

	public List<StatsPlayerTaupe> getTaupeTeamPlayers() {
		if (this.taupeTeam == 0) {
			return new ArrayList<>();
		}

		return StatsPlayerTaupe.getAllPlayerManager()
				.stream()
				.filter(p -> p.getTaupeTeam() == this)
				.collect(Collectors.toList());
	}

	public List<StatsPlayerTaupe> getSuperTaupeTeamPlayers() {
		if (this.superTaupeTeam == 0) {
			return new ArrayList<>();
		}

		return StatsPlayerTaupe.getAllPlayerManager()
				.stream()
				.filter(p -> p.getSuperTaupeTeam() == this)
				.collect(Collectors.toList());
	}

	public List<StatsPlayerTaupe> getPlayers(){
		return players;
	}

	public List<StatsPlayerTaupe> getAlivesPlayers() {
		return players.stream()
				.filter(StatsPlayerTaupe::isAlive)
				.collect(Collectors.toList());
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean pAlive) {
		alive = pAlive;
	}

	public boolean isFull() { return this.players.size() >= MAX_PLAYER_PER_TEAM; }

	public int getSize() {
		return this.players.size();
	}

	public long getTaupeTeamSize() {
		if (this.taupeTeam == 0) {
			return 0;
		}

		return StatsPlayerTaupe.getAllPlayerManager()
				.stream()
				.filter(p -> p.getTaupeTeam() == this)
				.count();
	}

	public long getSuperTaupeTeamSize() {
		if (this.superTaupeTeam == 0) {
			return 0;
		}

		return StatsPlayerTaupe.getAllPlayerManager()
				.stream()
				.filter(p -> p.getSuperTaupeTeam() == this)
				.count();
	}

	public String getName() {
		return this.name;
	}

	public List<Player> getConnectedPlayers() {
		return this.players.stream()
				.map(StatsPlayer::getPlayer)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	public List<Player> getPlayersInWorldEnvironment(World.Environment environment) {
		return this.getConnectedPlayers().stream()
				.filter(p -> p.getWorld().getEnvironment() == environment)
				.collect(Collectors.toList());
	}


	public void deleteTeam() {
		for (StatsPlayerTaupe pl : StatsPlayerTaupe.getAllPlayerManager()) {
			if (pl.getTeam() == this)
				pl.setTeam(null);
			if (pl.getStartTeam() == this)
				pl.setStartTeam(null);
			if (pl.getTaupeTeam() == this)
				pl.setTaupeTeam(null);
			if (pl.getSuperTaupeTeam() == this)
				pl.setSuperTaupeTeam(null);
		}
		InventoryTeamsElement.removeTeam(team.getName());
		teams.remove(this.team.getName());
		team.unregister();
	}

	public void joinTeam(StatsPlayerTaupe pl) {
		if ((!alive || team.getEntries().size() >= MAX_PLAYER_PER_TEAM) && !this.spectator)
			return;

		if (Objects.isNull(pl))
			return;

		if (pl.getTeam() != null) {
			pl.getTeam().leaveTeam(pl.getUuid());
		}

		Player player = Bukkit.getPlayer(pl.getUuid());
		if (Objects.nonNull(player)) {
			joinScoreboardTeam(pl.getName(), pl, player);
		}

		pl.setTeam(this);
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			pl.setStartTeam(this);
			reloadTeamsInventories();
		}
	}
	
	public void joinTeam(UUID uuid) {
		StatsPlayerTaupe pl = StatsPlayerTaupe.getPlayerManager(uuid);
		joinTeam(pl);
	}

	private void joinScoreboardTeam(String name, StatsPlayerTaupe pl, Player player) {
		team.addEntry(name);
		objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

		if (Objects.nonNull(pl)) {
			players.add(pl);
		}

		if (Objects.nonNull(player)) {
			Score score = objective.getScore(player);
			score.setScore(20);
			player.setScoreboard(board);
		}
	}

	public void leaveTeam(UUID uuid){
		StatsPlayerTaupe pl = StatsPlayerTaupe.getPlayerManager(uuid);
		Player player = Bukkit.getPlayer(uuid);
		
		team.removeEntry(pl.getName());
		players.remove(pl);
		if (Objects.nonNull(player)) {
			board.resetScores(player);
		}
		pl.setTeam(null);
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			pl.setStartTeam(null);
			reloadTeamsInventories();
		}
	}

	private void reloadTeamsInventories() {
		this.main.getScenariosManager().teamsMenu.reloadInventory();
		InventoryTeamsElement inventoryTeamsElement = InventoryTeamsElement.getInventoryTeamsElementOfTeam(this);
		if (Objects.nonNull(inventoryTeamsElement)) {
			inventoryTeamsElement.reloadInventory();
		}
		InventoryTeamsPlayers.reloadInventories();
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
	
	public static TeamCustom getTeamCustomByName(String name) {
		return teams.get(name);
	}

	public static int getNumberOfTeam() {
		return teams.size();
	}

	public static TeamCustom getSpectatorTeam() {
		return teams.values()
				.stream()
				.filter(TeamCustom::isSpectator)
				.findFirst()
				.orElse(null);
	}

	public static TeamCustom getTaupeTeam(int teamNumber) {
		return teams.values()
				.stream()
				.filter(team -> team.getTaupeTeamNumber() == teamNumber)
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
