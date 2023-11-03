package fr.thedarven.team.model;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.PlayerJoinTeamEvent;
import fr.thedarven.events.event.PlayerLeaveTeamEvent;
import fr.thedarven.events.event.TeamCreateEvent;
import fr.thedarven.events.event.TeamDeleteEvent;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.stats.model.StatsPlayer;
import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.stream.Collectors;

public class TeamCustom {

	public final static int MAX_PLAYER_PER_TEAM = 9;
	public final static int MAX_TEAM_AMOUNT = 36;

	private static final ScoreboardManager manager = Bukkit.getScoreboardManager();
	public static final Scoreboard board = manager.getNewScoreboard();
	private static final Objective objective = board.registerNewObjective("health", "health");
	
	private static final Map<String, TeamCustom> teams = new HashMap<>();

	private final TaupeGun main;

	private String name;
	private ColorEnum color;

	private final Team team;
	private final int taupeTeam;
	private final int superTaupeTeam;
	private final boolean isSpectator;
	private final List<PlayerTaupe> players;
	private boolean alive;
	
	public TeamCustom(TaupeGun main, String name, ColorEnum color, int pTaupe, int pSuperTaupe, boolean pSpectator, boolean pAlive) {
		this.main = main;
		team = board.registerNewTeam(name);
		if (name.startsWith(this.main.getTeamManager().getMoleTeamName()) || name.startsWith(this.main.getTeamManager().getSuperMoleTeamName())) {
			team.setPrefix(color.getColor() + "[" + name + "] ");
		} else {
			team.setPrefix(color.getColor());
		}
		team.setSuffix("§f");

		this.name = name;
		this.color = color;

		this.taupeTeam = pTaupe;
		this.superTaupeTeam = pSuperTaupe;
		this.isSpectator = pSpectator;
		this.players = new ArrayList<>();
		this.alive = pAlive;

		teams.put(name, this);

		TeamCreateEvent teamCreateEvent = new TeamCreateEvent(this);
		Bukkit.getPluginManager().callEvent(teamCreateEvent);
	}

	public ColorEnum getColor() {
		return color;
	}

	public void setColor(ColorEnum color) {
		this.color = color;
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
		return isSpectator;
	}

	public List<PlayerTaupe> getTaupeTeamPlayers() {
		if (this.taupeTeam == 0) {
			return new ArrayList<>();
		}

		return PlayerTaupe.getAllPlayerManager()
				.stream()
				.filter(p -> p.getTaupeTeam() == this)
				.collect(Collectors.toList());
	}

	public List<PlayerTaupe> getSuperTaupeTeamPlayers() {
		if (this.superTaupeTeam == 0) {
			return new ArrayList<>();
		}

		return PlayerTaupe.getAllPlayerManager()
				.stream()
				.filter(p -> p.getSuperTaupeTeam() == this)
				.collect(Collectors.toList());
	}

	public List<PlayerTaupe> getPlayers(){
		return players;
	}

	public List<PlayerTaupe> getAlivesPlayers() {
		return players.stream()
				.filter(PlayerTaupe::isAlive)
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

		return PlayerTaupe.getAllPlayerManager()
				.stream()
				.filter(p -> p.getTaupeTeam() == this)
				.count();
	}

	public long getSuperTaupeTeamSize() {
		if (this.superTaupeTeam == 0) {
			return 0;
		}

		return PlayerTaupe.getAllPlayerManager()
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
		for (PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
			if (pl.getTeam() == this) {
				pl.setTeam(null);

				PlayerLeaveTeamEvent playerLeaveTeamEvent = new PlayerLeaveTeamEvent(pl, this);
				Bukkit.getPluginManager().callEvent(playerLeaveTeamEvent);
			}
			if (pl.getStartTeam().isPresent() && pl.getStartTeam().get() == this) {
				pl.setStartTeam(null);
			}
			if (pl.getTaupeTeam() == this) {
				pl.setTaupeTeam(null);
			}
			if (pl.getSuperTaupeTeam() == this) {
				pl.setSuperTaupeTeam(null);
			}
		}

		teams.remove(this.team.getName());
		team.unregister();

		TeamDeleteEvent teamDeleteEvent = new TeamDeleteEvent(this);
		Bukkit.getPluginManager().callEvent(teamDeleteEvent);
	}

	public void joinTeam(PlayerTaupe pl) {
		if ((!alive || isFull()) && !this.isSpectator) {
			return;
		}

		if (Objects.isNull(pl)) {
			return;
		}

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
		}

		PlayerJoinTeamEvent playerJoinTeamEvent = new PlayerJoinTeamEvent(pl, this);
		Bukkit.getPluginManager().callEvent(playerJoinTeamEvent);
	}
	
	public void joinTeam(UUID uuid) {
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(uuid);
		joinTeam(pl);
	}

	private void joinScoreboardTeam(String name, PlayerTaupe pl, Player player) {
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
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(uuid);
		Player player = Bukkit.getPlayer(uuid);
		
		team.removeEntry(pl.getName());
		players.remove(pl);
		if (Objects.nonNull(player)) {
			board.resetScores(player);
		}
		pl.setTeam(null);

		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			pl.setStartTeam(null);
		}

		PlayerLeaveTeamEvent playerLeaveTeamEvent = new PlayerLeaveTeamEvent(pl, this);
		Bukkit.getPluginManager().callEvent(playerLeaveTeamEvent);
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
	
	public static Optional<TeamCustom> getTeamByName(String name) {
		return Optional.ofNullable(teams.get(name));
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

	public static int countTeam() {
		return teams.size();
	}
}
