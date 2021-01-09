package fr.thedarven.main.metier;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.thedarven.utils.UtilsClass;

public class PlayerTaupe extends PlayerCustom{
	
	private static Map<UUID, PlayerTaupe> playerManagerHashMap = new HashMap<>();
	
	private String name;
	private boolean alive;
	private int kill;
	private Location netherPortal;
	private InventoryManager opennedInventory;
	
	private TeamCustom team;
	private TeamCustom startTeam;
	private TeamCustom teamTaupe;
	private TeamCustom teamSuperTaupe;
	private String claim;
	
	private boolean canClick;
	private String createTeamName;
	private String createKitName;
	
	public PlayerTaupe(UUID playerUuid) {
		super(playerUuid);
		this.name = Bukkit.getPlayer(playerUuid).getName();
		
		Player player = Bukkit.getPlayer(playerUuid);
		if(!EnumGameState.isCurrentState(EnumGameState.WAIT, EnumGameState.LOBBY)) {
			alive = false;
			team = TeamCustom.getSpectatorTeam();
			team.joinTeam(name);
			
			player.setGameMode(GameMode.SPECTATOR);
		}else {
			alive = true;
			team = null;
			
			player.setHealth(20);
			player.setLevel(0);
			player.setGameMode(GameMode.SURVIVAL);
			player.teleport(new Location(UtilsClass.getWorld(), 0.5, 201, 0.5));
		}
		startTeam = null;
		
		kill = 0;
		netherPortal = new Location(UtilsClass.getWorldNether(),0.0,0.0,0.0);
		opennedInventory = new InventoryManager();
		
		teamTaupe = null;
		teamSuperTaupe = null;
		claim = "aucun";
		canClick = true;
		createTeamName = null;
		createKitName = null;
		
		playerManagerHashMap.put(this.uuid, this);
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public boolean isSpectator() {
		return startTeam != TeamCustom.getSpectatorTeam();
	}
	
	public String getName() {
		return name;
	}
	
	public TeamCustom getTeam() {
		return team;
	}
	
	public TeamCustom getStartTeam() {
		return startTeam;
	}
	
	public int getKill() {
		return kill;
	}
	
	public Location getNetherPortal() {
		return netherPortal;
	}
	
	public InventoryManager getOpennedInventory() {
		return this.opennedInventory;
	}
	
	public void setAlive(boolean value) {
		alive = value;
	}
	
	public void setCustomName(String value) {
		name = value;
	}
	
	public void setKill(int value) {
		kill = value;
	}
	
	public void setTeam(TeamCustom pTeam) {
		team = pTeam;
	}
	
	public void setStartTeam(TeamCustom pTeam) {
		startTeam = pTeam;
	}
	
	public void setNetherPortal(Location loc) {
		netherPortal = loc;
	}
	
	public void setClaimTaupe(String claimParametre) {
		this.claim = claimParametre;
	}
	
	
	
	public boolean isTaupe() {
		return teamTaupe != null;
	}
	
	public boolean isSuperTaupe() {
		return teamSuperTaupe != null;
	}
	
	public TeamCustom getTaupeTeam() {
		return teamTaupe;
	}
	
	public TeamCustom getSuperTaupeTeam() {
		return teamSuperTaupe;
	}
	
	public int getTaupeTeamNumber() {
		if(isTaupe())
			return teamTaupe.getTaupeTeamNumber();
		return 0;
	}
	
	public int getSuperTaupeTeamNumber() {
		if(isSuperTaupe())
			return teamSuperTaupe.getSuperTaupeTeamNumber();
		return 0;
	}
	
	public void setTaupeTeam(TeamCustom pTeam) {
		teamTaupe = pTeam;
	}
	
	public void setSuperTaupeTeam(TeamCustom pTeam) {
		teamSuperTaupe = pTeam;
	}
	
	public boolean isReveal() {
		return (isTaupe() && isAlive() && (team == teamTaupe || team == teamSuperTaupe));
	}
	
	public boolean isSuperReveal() {
		return (isSuperTaupe() && isAlive() && team == teamSuperTaupe);
	}

	public String getClaimTaupe() {
		return claim;
	}
	
	public boolean hasWin() {
		Optional<TeamCustom> optionalVictoryTeam = TeamCustom.getWinTeam();
		if(!optionalVictoryTeam.isPresent())
			return false;
		TeamCustom victoryTeam = optionalVictoryTeam.get();
		
		if(victoryTeam.isSuperTaupeTeam())
			return victoryTeam == this.teamSuperTaupe;
		else if(victoryTeam.isTaupeTeam())
			return victoryTeam == this.teamTaupe && this.teamSuperTaupe == null;
		else
			return victoryTeam == this.startTeam && this.teamTaupe == null;
	}
	

	/*
	 * CONFIG
	 */
	public boolean getCanClick() {
		return this.canClick;
	}
	
	public String getCreateTeamName() {
		return createTeamName;
	}
	
	public String getCreateKitName() {
		return createKitName;
	}
	
	public void setCanClick(boolean pCanClick) {
		this.canClick = pCanClick;
	}
	
	public void setCreateTeamName(String pName) {
		createTeamName = pName;
	}
	
	public void setCreateKitName(String pName) {
		createKitName = pName;
	}
	
	public static PlayerTaupe getPlayerTaupeByName(String name) {
		if (Objects.isNull(name))
			return null;

		Optional<PlayerTaupe> playerTaupeOptional = playerManagerHashMap.values()
				.stream()
				.filter(pl -> pl.getName().equals(name))
				.findFirst();
		return playerTaupeOptional.orElse(null);
	}
	
	public static PlayerTaupe getPlayerManager(UUID playerUuid) {
		if(playerManagerHashMap.containsKey(playerUuid)) {
			return playerManagerHashMap.get(playerUuid);
		}
		return new PlayerTaupe(playerUuid);
	}
	
	public static List<PlayerTaupe> getAlivePlayerManager(){
		List<PlayerTaupe> list = new ArrayList<>();
		for(PlayerTaupe pc : playerManagerHashMap.values()){
			if(pc.isAlive()){
				list.add(pc);
			}
		}
		return list;
	}
	
	public static List<PlayerTaupe> getDeathPlayerManager(){
		List<PlayerTaupe> list = new ArrayList<>();
		for(PlayerTaupe pc : playerManagerHashMap.values()){
			if(!pc.isAlive()){
				list.add(pc);
			}
		}
		return list;
	}
	
	public static List<PlayerTaupe> getAllPlayerManager(){
		List<PlayerTaupe> list = new ArrayList<>(playerManagerHashMap.values());
		return list;
	}
	
}
