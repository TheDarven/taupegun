package fr.thedarven.utils.api.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;

public class PersonalScoreboard {

	private TaupeGun main;
	private Player p;
	private final UUID uuid;
	private final ObjectiveSign objectiveSign;

	PersonalScoreboard(Player player, TaupeGun main){
		this.main = main;
		this.p = player;
		uuid = player.getUniqueId();
		objectiveSign = new ObjectiveSign("sidebar", "TaupeGun");

		reloadData();
		objectiveSign.addReceiver(player);
	}

	public void reloadData(){}

	public void setLines(String ip){
		int timer = TaupeGun.getInstance().getGameManager().getTimer();

		PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
		int i = 0;
		Location loc = p.getLocation();
		int distance;
		int playerTeam = 0;
		for(TeamCustom team : TeamCustom.getAllStartAliveTeams())
			playerTeam += team.getTeam().getEntries().size();
		
		objectiveSign.removeAllLine(this.main.getScenariosManager().wallShrinkingTime.getValue() - timer > 0 ? 9 : 8);
		
		objectiveSign.setDisplayName("§6TaupeGun");
		objectiveSign.setLine(i++, "§1");
		if (this.main.getScenariosManager().episode.getValue() > 0) {
			int episodeNumber = timer / this.main.getScenariosManager().episode.getIntValue() + 1;
			
			Map<String, String> params = new HashMap<>();
			params.put("episodeNumber", episodeNumber+"");
			String episodeMessage = "§7⋙ §e"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "episode", true), params);

			objectiveSign.setLine(i++, episodeMessage);
		}
		
		// WALL
		String timerS;
		Map<String, String> params = new HashMap<>();
		if (this.main.getScenariosManager().wallShrinkingTime.getValue() - timer > 0) {
			if (this.main.getScenariosManager().wallShrinkingTime.getValue() - timer >= 6000){
				timerS = DurationFormatUtils.formatDuration((this.main.getScenariosManager().wallShrinkingTime.getIntValue() - timer) * 1000, "mmm:ss");
			} else {
				timerS = DurationFormatUtils.formatDuration((this.main.getScenariosManager().wallShrinkingTime.getIntValue() - timer) * 1000, "mm:ss");
			}
			params.put("valueColor", "§e");
			params.put("endValueColor", "§f");
			params.put("timer", timerS);
			String wallMessage = "§l§7⋙ §f" + TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "wall", true), params);
			objectiveSign.setLine(i++, wallMessage);
		}
		
		// TIMER
		if (timer >= 6000){
			timerS = DurationFormatUtils.formatDuration(timer * 1000, "mmm:ss");
		} else {
			timerS = DurationFormatUtils.formatDuration(timer * 1000, "mm:ss");
		}
		params.clear();
		params.put("valueColor", "§e");
		params.put("endValueColor", "§f");
		params.put("timer", timerS);
		String timerMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "timer", true), params);
		objectiveSign.setLine(i++, timerMessage);	
		objectiveSign.setLine(i++, "§2");
		
		// PLAYER COUNTER
		params.clear();
		params.put("valueColor", "§e");
		params.put("endValueColor", "§f");
		params.put("playerCounter", playerTeam+"");
		params.put("teamCounter", TeamCustom.getAllStartAliveTeams().size()+"");
		String connectedPlayerMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "connectedPlayer",true), params);
		objectiveSign.setLine(i++, connectedPlayerMessage);
		// objectiveSign.setLine(i++, "Joueurs:§e "+playerTeam+" ("+TeamCustom.getAllStartAliveTeams().size()+")");

		
		
		// KILL
		params.clear();
		params.put("valueColor", "§e");
		params.put("endValueColor", "§f");
		params.put("kill", pc.getKill()+"");
		String killMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "kill", true), params);
		objectiveSign.setLine(i++, killMessage);
		objectiveSign.setLine(i++, "§3");
		
		// DISTANCE
		if (p.getWorld() == this.main.getWorldManager().getWorldNether()){
			Location portailLocation = PlayerTaupe.getPlayerManager(p.getUniqueId()).getNetherPortal();
			// distance = (int) Math.sqrt((portailLocation.getX() - loc.getBlockX())*(portailLocation.getX() - loc.getBlockX()) + (portailLocation.getZ() - loc.getBlockZ())*(portailLocation.getZ() - loc.getBlockZ()) + (portailLocation.getY() - loc.getBlockY())*(portailLocation.getY() - loc.getBlockY()));
			distance = (int) portailLocation.distance(loc);
			params.clear();
			params.put("valueColor", "§e");
			params.put("endValueColor", "§f");
			params.put("distance", distance+"");
			String portalMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "portal", true), params);
			objectiveSign.setLine(i++, portalMessage);
		}else {
			distance = (int) Math.sqrt(loc.getX() * loc.getX() + loc.getZ()* loc.getZ());
			params.clear();
			params.put("valueColor", "§e");
			params.put("endValueColor", "§f");
			params.put("distance", distance+"");
			String centerMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "center", true), params);
			objectiveSign.setLine(i++, centerMessage);
		}
		// BORDER
		String border = "?";
		World world = this.main.getWorldManager().getWorld();
		if (Objects.nonNull(world)) {
			border = (int) (world.getWorldBorder().getSize()/2) + "";
		}
		params.clear();
		params.put("valueColor", "§e");
		params.put("endValueColor", "§f");
		params.put("border", border);
		String borderMessage = "§l§7⋙ §f"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("SCOREBOARD", "border",true), params);
		objectiveSign.setLine(i++, borderMessage);
		objectiveSign.updateLines();
	}

	public void onLogout(){
		objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
	}
}

