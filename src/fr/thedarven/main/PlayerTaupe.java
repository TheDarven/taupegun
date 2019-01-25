package fr.thedarven.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import fr.thedarven.events.Teams;

public class PlayerTaupe {
	private static Map<UUID, PlayerTaupe> playerManagerHashMap = new HashMap<>();
	private UUID uuid;
	private String name;
	private boolean alive;
	private int kill;
	private String teamName;
	private Location netherPortal;
	
	private int taupe;
	private int superTaupe;
	private String claim;
	private boolean revealTaupe;
	private boolean revealSuperTaupe;
	
	private boolean canClick;
	private String createTeamName;
	
	public PlayerTaupe(UUID playerUuid) {
		this.uuid = playerUuid;
		this.name = Bukkit.getPlayer(playerUuid).getName();
		if(!TaupeGun.etat.equals(EnumGame.WAIT) && !TaupeGun.etat.equals(EnumGame.LOBBY)) {
			alive = false;
			Teams.joinInitTeam("Spectateurs", this.name);
			Bukkit.getPlayer(playerUuid).setGameMode(GameMode.SPECTATOR);
		}else {
			alive = true;
			Location lobby_spawn = new Location(Bukkit.getWorld("world"), 0.5, 201, 0.5);
			Bukkit.getPlayer(playerUuid).teleport(lobby_spawn);
			Bukkit.getPlayer(playerUuid).setHealth(20);
			Bukkit.getPlayer(playerUuid).setLevel(0);
			Bukkit.getPlayer(playerUuid).getInventory().clear();
			Bukkit.getPlayer(playerUuid).setGameMode(GameMode.SURVIVAL);
		}
		kill = 0;
		teamName = "aucune";
		netherPortal = new Location(Bukkit.getWorld("world_nether"),0.0,0.0,0.0);
		
		taupe = -1;
		superTaupe = -1;
		claim = "aucun";
		revealTaupe = false;
		revealSuperTaupe = false;
		canClick = true;
		createTeamName = null;
		
		playerManagerHashMap.put(this.uuid, this);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public String getCustomName() {
		return name;
	}
	
	public int getKill() {
		return kill;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public Location getNetherPortal() {
		return netherPortal;
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
	
	public void setTeamName(String value) {
		teamName = value;
	}
	
	public void setNetherPortal(Location loc) {
		netherPortal = loc;
	}
	
	public void setClaimTaupe(String claimParametre) {
		this.claim = claimParametre;
	}
	
	
	
	public boolean isTaupe() {
		if(taupe == -1) {
			return false;
		}
		return true;
	}
	
	public boolean isSuperTaupe() {
		if(superTaupe == -1) {
			return false;
		}
		return true;
	}
	
	public int getTaupeTeam() {
		return taupe;
	}
	
	public int getSuperTaupeTeam() {
		return superTaupe;
	}
	
	public void setTaupeTeam(int team) {
		taupe = team;
	}
	
	public void setSuperTaupeTeam(int team) {
		superTaupe = team;
	}
	
	public boolean revealTaupe() {
		if(!revealTaupe && isTaupe() && isAlive()) {
			revealTaupe = true;
			return true;
		}
		return false;
	}
	
	public boolean revealSuperTaupe() {
		if(!revealSuperTaupe && isSuperTaupe() && isAlive()) {
			revealSuperTaupe = true;
			return true;
		}
		return false;
	}
	
	public String getClaimTaupe() {
		return claim;
	}
	
	
	
	
	public boolean getCanClick() {
		return this.canClick;
	}
	
	public String getCreateTeamName() {
		return createTeamName;
	}
	
	public void setCanClick(boolean pCanClick) {
		this.canClick = pCanClick;
	}
	
	public void setCreateTeamName(String pName) {
		createTeamName = pName;
	}
	
	
	
	
	public static PlayerTaupe getPlayerManager(UUID playerUuid) {
		if(playerManagerHashMap.containsKey(playerUuid)) {
			return playerManagerHashMap.get(playerUuid);
		}
		return new PlayerTaupe(playerUuid);
	}
	
	public static List<PlayerTaupe> getAlivePlayerManager(){
		List<PlayerTaupe> list = new ArrayList<PlayerTaupe>();
		for(PlayerTaupe pc : playerManagerHashMap.values()){
			if(pc.isAlive()){
				list.add(pc);
			}
		}
		return list;
	}
	
	public static List<PlayerTaupe> getDeathPlayerManager(){
		List<PlayerTaupe> list = new ArrayList<PlayerTaupe>();
		for(PlayerTaupe pc : playerManagerHashMap.values()){
			if(!pc.isAlive()){
				list.add(pc);
			}
		}
		return list;
	}
	
	public static List<PlayerTaupe> getAllPlayerManager(){
		List<PlayerTaupe> list = new ArrayList<PlayerTaupe>(playerManagerHashMap.values());
		return list;
	}
	
}
