package fr.thedarven.main.metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.thedarven.main.TaupeGun;

public class PlayerTaupe {
	private static Map<UUID, PlayerTaupe> playerManagerHashMap = new HashMap<>();
	private UUID uuid;
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
		this.uuid = playerUuid;
		this.name = Bukkit.getPlayer(playerUuid).getName();
		if(!TaupeGun.etat.equals(EnumGame.WAIT) && !TaupeGun.etat.equals(EnumGame.LOBBY)) {
			alive = false;
			team = TeamCustom.getSpectatorTeam();
			team.joinTeam(name);
			Bukkit.getPlayer(playerUuid).setGameMode(GameMode.SPECTATOR);
		}else {
			alive = true;
			Location lobby_spawn = new Location(Bukkit.getWorld("world"), 0.5, 201, 0.5);
			Bukkit.getPlayer(playerUuid).teleport(lobby_spawn);
			team = null;
			
			Bukkit.getPlayer(playerUuid).setHealth(20);
			Bukkit.getPlayer(playerUuid).setLevel(0);
			Bukkit.getPlayer(playerUuid).setGameMode(GameMode.SURVIVAL);
		}
		startTeam = null;
		
		kill = 0;
		netherPortal = new Location(Bukkit.getWorld("world_nether"),0.0,0.0,0.0);
		opennedInventory = new InventoryManager();
		
		teamTaupe = null;
		teamSuperTaupe = null;
		claim = "aucun";
		canClick = true;
		createTeamName = null;
		createKitName = null;
		
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
	
	public Player getPlayer(){
		if(isOnline()){
			return Bukkit.getPlayer(uuid);
		}
		return null;
	}
	
	public boolean isOnline(){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getUniqueId().equals(this.uuid)){
				return true;
			}
		}
		return false;
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
	
	public boolean revealTaupe() {
		if(!isReveal())
			return true;
		return false;
	}
	
	public boolean revealSuperTaupe() {
		if(!isSuperReveal())
			return true;
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
