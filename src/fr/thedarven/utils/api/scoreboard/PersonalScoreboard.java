package fr.thedarven.utils.api.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class PersonalScoreboard {
	private Player p;
	private final UUID uuid;
	private final ObjectiveSign objectiveSign;

	PersonalScoreboard(Player player){
		this.p = player;
		uuid = player.getUniqueId();
		objectiveSign = new ObjectiveSign("sidebar", "TaupeGun");

		reloadData();
		objectiveSign.addReceiver(player);
	}

	public void reloadData(){}

	public void setLines(String ip){
		PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
		int i = 0;
		Location loc = p.getLocation();
		int distance = 0;
		int playerTeam = 0;
		for(TeamCustom team : TeamCustom.getAllStartAliveTeams())
			playerTeam += team.getTeam().getEntries().size();
		
		objectiveSign.removeAllLine(InventoryRegister.murtime.getValue()*60-TaupeGun.timer > 0 ? 9 : 8);
		
		objectiveSign.setDisplayName("§6TaupeGun");		
		objectiveSign.setLine(i++, "§1");
		if(InventoryRegister.episode.getValue() > 0) {
			int episodeNumber = (TaupeGun.timer/InventoryRegister.episode.getValue()*60)+1;
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("episodeNumber", episodeNumber+"");
			String episodeMessage = "§7⋙ §e"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "episode", InventoryRegister.language.getSelectedLanguage(), true), params);

			objectiveSign.setLine(i++, episodeMessage);
		}
		
		// WALL
		String timer;
		Map<String, String> params = new HashMap<String, String>();
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer > 0) {
			if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer >= 6000){
				timer = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mmm:ss");
				// objectiveSign.setLine(i++, "Mur:§e "+DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mmm:ss"));
			}else{
				timer = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mm:ss");
				// objectiveSign.setLine(i++, "Mur:§e "+DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mm:ss"));
			}
			params.clear();
			params.put("valueColor", "§e");
			params.put("endValueColor", "§f");
			params.put("timer", timer);
			String wallMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "wall", InventoryRegister.language.getSelectedLanguage(), true), params);
			objectiveSign.setLine(i++, wallMessage);
		}
		
		// TIMER
		if(TaupeGun.timer >= 6000){
			// objectiveSign.setLine(i++, "Chrono:§e "+DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mmm:ss"));
			timer = DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mmm:ss");
		}else{
			// objectiveSign.setLine(i++, "Chrono:§e "+DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mm:ss"));
			timer = DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mm:ss");
		}
		params.clear();
		params.put("valueColor", "§e");
		params.put("endValueColor", "§f");
		params.put("timer", timer);
		String timerMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "timer", InventoryRegister.language.getSelectedLanguage(), true), params);
		objectiveSign.setLine(i++, timerMessage);	
		objectiveSign.setLine(i++, "§2");
		
		// PLAYER COUNTER
		params.clear();
		params.put("valueColor", "§e");
		params.put("endValueColor", "§f");
		params.put("playerCounter", playerTeam+"");
		params.put("teamCounter", TeamCustom.getAllStartAliveTeams().size()+"");
		String connectedPlayerMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "connectedPlayer", InventoryRegister.language.getSelectedLanguage(), true), params);
		objectiveSign.setLine(i++, connectedPlayerMessage);
		// objectiveSign.setLine(i++, "Joueurs:§e "+playerTeam+" ("+TeamCustom.getAllStartAliveTeams().size()+")");

		
		
		// KILL
		params.clear();
		params.put("valueColor", "§e");
		params.put("endValueColor", "§f");
		params.put("kill", pc.getKill()+"");
		String killMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "kill", InventoryRegister.language.getSelectedLanguage(), true), params);
		objectiveSign.setLine(i++, killMessage);
		objectiveSign.setLine(i++, "§3");
		
		// DISTANCE
		if(p.getWorld().getName().equals("world_nether")){
			Location portailLocation = PlayerTaupe.getPlayerManager(p.getUniqueId()).getNetherPortal();
			// distance = (int) Math.sqrt((portailLocation.getX() - loc.getBlockX())*(portailLocation.getX() - loc.getBlockX()) + (portailLocation.getZ() - loc.getBlockZ())*(portailLocation.getZ() - loc.getBlockZ()) + (portailLocation.getY() - loc.getBlockY())*(portailLocation.getY() - loc.getBlockY()));
			distance = (int) portailLocation.distance(loc);
			params.clear();
			params.put("valueColor", "§e");
			params.put("endValueColor", "§f");
			params.put("distance", distance+"");
			String portalMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "portal", InventoryRegister.language.getSelectedLanguage(), true), params);
			objectiveSign.setLine(i++, portalMessage);
		}else {
			distance = (int) Math.sqrt(loc.getX() * loc.getX() + loc.getZ()* loc.getZ());
			params.clear();
			params.put("valueColor", "§e");
			params.put("endValueColor", "§f");
			params.put("distance", distance+"");
			String centerMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "center", InventoryRegister.language.getSelectedLanguage(), true), params);
			objectiveSign.setLine(i++, centerMessage);
		}
		// BORDER
		int border = (int) (Bukkit.getServer().getWorld("world").getWorldBorder().getSize()/2);
		params.clear();
		params.put("valueColor", "§e");
		params.put("endValueColor", "§f");
		params.put("border", border+"");
		String borderMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "border", InventoryRegister.language.getSelectedLanguage(), true), params);
		objectiveSign.setLine(i++, borderMessage);
		objectiveSign.updateLines();
	}

	public void onLogout(){
		objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
	}
}

