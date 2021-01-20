package fr.thedarven.models;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.scenarios.teams.InventoryDeleteTeams;
import fr.thedarven.scenarios.teams.InventoryTeamsElement;
import fr.thedarven.scenarios.teams.InventoryTeamsParameters;
import fr.thedarven.scenarios.teams.InventoryTeamsPlayers;
import fr.thedarven.utils.CodeColor;
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

	private String name;
	private Team team;
	private int taupeTeam;
	private int superTaupeTeam;
	private boolean spectator;
	private List<PlayerTaupe> players;
	private boolean alive;
	
	public TeamCustom(String name, int pColor, int pTaupe, int pSuperTaupe, boolean pSpectator, boolean pAlive) {
		team = board.registerNewTeam(name);
		team.setPrefix("§" + CodeColor.codeColorBP(pColor));
		team.setSuffix("§f");

		this.name = name;
		this.taupeTeam = pTaupe;
		this.superTaupeTeam = pSuperTaupe;
		this.spectator = pSpectator;
		this.players = new ArrayList<>();
		this.alive = pAlive;
		
		InventoryTeamsElement inv = new InventoryTeamsElement(name, CodeColor.codeColorPB(CodeColor.codeColorBP(pColor)));
		new InventoryTeamsParameters(inv);
		new InventoryTeamsPlayers(inv);
		new InventoryDeleteTeams(inv);

		teams.put(name, this);
	}
	
	public TeamCustom(String name, String pColor, int pTaupe, int pSuperTaupe, boolean pSpectator, boolean pAlive) {
		team = board.registerNewTeam(name);
		if (name.startsWith(TaupeGun.getInstance().getTeamManager().getMoleTeamName()) || name.startsWith(TaupeGun.getInstance().getTeamManager().getSuperMoleTeamName()))
			team.setPrefix("§" + pColor + "[" + name + "] ");
		else
			team.setPrefix("§" + pColor);
		team.setSuffix("§f");

		this.name = name;
		this.taupeTeam = pTaupe;
		this.superTaupeTeam = pSuperTaupe;
		this.spectator = pSpectator;
		this.players = new ArrayList<>();
		this.alive = pAlive;
		
		InventoryTeamsElement inv = new InventoryTeamsElement(name, CodeColor.codeColorPB(pColor));
		new InventoryTeamsParameters(inv);
		new InventoryTeamsPlayers(inv);
		new InventoryDeleteTeams(inv);
		
		teams.put(name, this);
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

	public String getName() {
		return this.name;
	}

	public List<Player> getConnectedPlayers() {
		return this.players.stream()
				.map(PlayerCustom::getPlayer)
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

	public void joinTeam(PlayerTaupe pl) {
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

	// TODO Regarder usecase
	public void joinTeam(UUID uuid) {
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(uuid);
		joinTeam(pl);
	}

	public void joinTeam(String pseudo) {
		Player player = Bukkit.getPlayer(pseudo);
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
		joinScoreboardTeam(pseudo, pl, player);

		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			pl.setStartTeam(this);
			reloadTeamsInventories();
		}
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
			reloadTeamsInventories();
		}
	}

	private void reloadTeamsInventories() {
		TaupeGun.getInstance().getScenariosManager().teamsMenu.reloadInventory();
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
