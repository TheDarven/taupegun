package fr.thedarven.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.api.ScoreboardSign;

public class ScoreboardModule {
	
	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	public static Scoreboard board = manager.getNewScoreboard();
	public static Map<Player, ScoreboardSign> boards = new HashMap<>();
	public static ArrayList<ArrayList<Object>> scores = new ArrayList<ArrayList<Object>>();
	
	public ScoreboardModule() {
		addElement("§a",true);
		addElement("Joueurs",true);
		addElement("Centre",true);
		addElement("Kills",true);
		addElement("§b",true);
		addElement("Mur",true);
		addElement("Chrono",true);
		addElement("§c",true);
		addElement("Bordures",true);
	}
	
	private static void addElement(String pName, boolean pValue) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(pName);
		list.add(pValue);
		scores.add(list);
	}
	
	public static void modifyElement(String pName, boolean pValue) {
		for(ArrayList<Object> list : scores) {
			if(((String) list.get(0)).equals(pName) && (Boolean) list.get(1) != pValue){
				list.set(1, pValue);
				for(Entry<Player, ScoreboardSign> sign : boards.entrySet()){
					boards.get(sign.getKey()).destroy();
					boards.remove(sign.getKey());
					joinScoreboard(sign.getKey());
				}
				break;
			}
		}
	}
	
	
	
	public static void joinScoreboard(Player p) {
		ScoreboardSign scoreboard = new ScoreboardSign(p, "§6=== TaupeGun ===");
		scoreboard.create();
		
		int position = 1;
		
		for(ArrayList<Object> list : scores) {
			if((Boolean) list.get(1)) {
				switch((String) list.get(0)) {
					case "Joueurs":
						scoreboard.setLine(position, getJoueurs());
						break;
					case "Centre":
						scoreboard.setLine(position, getCentre(p));
						break;
					case "Kills":
						scoreboard.setLine(position, getKills(p));
						break;
					case "Mur":
						scoreboard.setLine(position, getMur());
						break;
					case "Chrono":
						scoreboard.setLine(position, getChrono());
						break;
					case "Bordures":
						scoreboard.setLine(position, getBordures());
						break;
					default:
						scoreboard.setLine(position, (String) list.get(0));
						break;
				}
				position+=1;
			}
		}	
		boards.put(p, scoreboard);
	}
	
	
	
	
	private static String getJoueurs() {
		int playerTeam = 0;
		int teamNick = 0;
		Set<Team> teams = Teams.board.getTeams();
		for(Team team : teams){
			if(team.getName().equals("Spectateurs") || team.getName().equals("Taupes1") || team.getName().equals("Taupes2") || team.getName().equals("SuperTaupe")){
				teamNick++;
			}
			if(team.getName() != "Spectateurs"){
				playerTeam += team.getEntries().size();	
			}
		}
		return getPrefixFixed("Joueurs")+"Joueurs :§e "+playerTeam+" ("+(Teams.board.getTeams().size()-teamNick)+")";
	}
	
	private static String getCentre(Player p) {
		Location loc = p.getLocation();
		int distance = 0;
		if(p.getWorld().getName().equals("world_nether")){
			Location portailLocation = PlayerTaupe.getPlayerManager(p.getUniqueId()).getNetherPortal();
			distance = (int) Math.sqrt((portailLocation.getX() - loc.getBlockX())*(portailLocation.getX() - loc.getBlockX()) + (portailLocation.getZ() - loc.getBlockZ())*(portailLocation.getZ() - loc.getBlockZ()) + (portailLocation.getY() - loc.getBlockY())*(portailLocation.getY() - loc.getBlockY()));
			return getPrefixFixed("Centre")+"Portail :§e "+distance;
		}else{
			distance = (int) Math.sqrt(loc.getX() * loc.getX() + loc.getZ()* loc.getZ());
			return getPrefixFixed("Centre")+"Centre :§e "+distance;
		}
	}
	
	private static String getKills(Player p) {
		return getPrefixFixed("Kills")+"Kills :§e "+PlayerTaupe.getPlayerManager(p.getUniqueId()).getKill();
	}
	
	private static String getMur() {
		String dateformatMur = "";
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer == 0) {
			modifyElement("Mur",false);
			return "";
		}
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer >= 6000){
			dateformatMur = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mmm:ss");
		}else{
			dateformatMur = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mm:ss");
		}
		return getPrefixFixed("Mur")+"Mur :§e "+dateformatMur;
	}
	
	public static String getChrono() {
		String dateformatChrono = "";
		if(TaupeGun.timer >= 6000){
			dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mmm:ss");
		}else{
			dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mm:ss");
		}
		return getPrefixFixed("Chrono")+"Chrono :§e "+dateformatChrono;
		//return "Chrono :§e "+dateformatChrono;
	}
	
	private static String getBordures() {
		return getPrefixFixed("Bordures")+"Bordures :§e "+(int) (Bukkit.getServer().getWorld("world").getWorldBorder().getSize()/2);
	}
	
	
	
	
	public static void setJoueurs(Player p) {
		boards.get(p).setLine(getLine("Joueurs"),getJoueurs());
	}
	
	public static void setCentre(Player p) {
		boards.get(p).setLine(getLine("Centre"),getCentre(p));
	}
	
	public static void setMur(Player p) {
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer > 0) {
			int a = getLine("Mur");
			String b = getMur();
			boards.get(p).setLine(a,b);
		}else if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer == 0) {
			modifyElement("Mur",false);
		}
	}
	
	public static void setChrono(Player p) {
		int a = getLine("Chrono");
		String b = getChrono();	
		boards.get(p).setLine(a,b);
	}
	
	public static void setBordures(Player p) {
		int a = getLine("Bordures");
		String b = getBordures();
		boards.get(p).setLine(a,b);
	}
	
	
	
	private static String getPrefixFixed(String pValue) {
		/* int position = 0;
		String prefix[] = {"➊ ","➋ ","➌ ","➍ ","➎ ","➏ ","➐ ","➑ ","➒ ","➓ "};
		for(ArrayList<Object> list : scores) {
			if((Boolean) list.get(1) && !((String) list.get(0)).startsWith("§")) {
				if(((String) list.get(0)).equals(pValue)) {
					break;
				}else{
					position += 1;
				}
			}
		}
		return prefix[position]; */
		return "";
	}

	public static int getLine(String pValue) {
		int position = 1;
		
		for(ArrayList<Object> list : scores) {
			if(((String) list.get(0)).equals(pValue)) {
				return position;
			}
			if((Boolean) list.get(1)) {
				position+=1;
			}
		}
		return 0;
	}
	
}
