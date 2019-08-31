package fr.thedarven.utils.api.scoreboard;

import java.util.UUID;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.TeamCustom;

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
		
		objectiveSign.setDisplayName("§6=== TaupeGun ===");		
		objectiveSign.setLine(i++, "§1");
		objectiveSign.setLine(i++, "Joueurs:§e "+playerTeam+" ("+TeamCustom.getAllStartAliveTeams().size()+")");
		if(p.getWorld().getName().equals("world_nether")){
			Location portailLocation = PlayerTaupe.getPlayerManager(p.getUniqueId()).getNetherPortal();
			distance = (int) Math.sqrt((portailLocation.getX() - loc.getBlockX())*(portailLocation.getX() - loc.getBlockX()) + (portailLocation.getZ() - loc.getBlockZ())*(portailLocation.getZ() - loc.getBlockZ()) + (portailLocation.getY() - loc.getBlockY())*(portailLocation.getY() - loc.getBlockY()));
			objectiveSign.setLine(i++, "Portail:§e "+distance);
		}else {
			distance = (int) Math.sqrt(loc.getX() * loc.getX() + loc.getZ()* loc.getZ());
			objectiveSign.setLine(i++, "Centre:§e "+distance);
		}
		objectiveSign.setLine(i++, "Kills:§e "+pc.getKill());
		objectiveSign.setLine(i++, "§2");
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer > 0) {
			if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer >= 6000){
				objectiveSign.setLine(i++, "Mur:§e "+DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mmm:ss"));
			}else{
				objectiveSign.setLine(i++, "Mur:§e "+DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mm:ss"));
			}
		}
		if(TaupeGun.timer >= 6000){
			objectiveSign.setLine(i++, "Chrono:§e "+DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mmm:ss"));
		}else{
			objectiveSign.setLine(i++, "Chrono:§e "+DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mm:ss"));
		}
		objectiveSign.setLine(i++, "§3");
		objectiveSign.setLine(i++, "Bordure:§e "+(int) (Bukkit.getServer().getWorld("world").getWorldBorder().getSize()/2));
		objectiveSign.updateLines();
	}

	public void onLogout(){
		objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
	}
}

