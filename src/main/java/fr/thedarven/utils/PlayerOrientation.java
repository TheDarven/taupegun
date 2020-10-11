package fr.thedarven.utils;

import java.util.Set;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.teams.TeamUtils;

public class PlayerOrientation {
	
	public static String Orientation(Player p){
		String bar = "";
		String spectatorTeamName = TeamUtils.getSpectatorTeamName();
		
		Set<Team> teams = TeamCustom.board.getTeams();
		for(Team team : teams){
			if(team.getEntries().contains(p.getName()) && !team.getName().equals(spectatorTeamName)){
				for(String player : team.getEntries()){
					if(!player.equals(p.getName())){
						bar = bar + getOrientation(p, player);
					}
				}
			}
		}
		return bar;
	}
	
	public static String getOrientation(Player p, String name){
		String orientation = ChatColor.BOLD+"";
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.getName().equals(name)){
				double oppose = player.getLocation().getZ()-p.getLocation().getZ();
				oppose = Math.sqrt(oppose*oppose);
				double adjacent = player.getLocation().getX()-p.getLocation().getX();
				adjacent = Math.sqrt(adjacent*adjacent);
				double angle = Math.atan(oppose/adjacent)*(180/Math.PI);
				
				int playerOrientation = 0;
				int seeOrientation = 0;
				if(player.getLocation().getX() >= p.getLocation().getX()){
					if(player.getLocation().getZ() >= p.getLocation().getZ()){
						if(angle <= 30.0){
							playerOrientation = 2;
						}else if(angle <= 60.0){
							playerOrientation = 3;
						}else{
							playerOrientation = 4;
						}
					}else{
						if(angle <= 30.0){
							playerOrientation = 2;
							
						}else if(angle <= 60.0){
							playerOrientation = 1;
						}else{
							playerOrientation = 0;
						}
					}
				}else if(player.getLocation().getX() < p.getLocation().getX()){
					if(player.getLocation().getZ() >= p.getLocation().getZ()){
						if(angle <= 30.0){
							playerOrientation = 6;
						}else if(angle <= 60.0){
							playerOrientation = 5;
						}else{
							playerOrientation = 4;
						}
					}else{
						if(angle <= 30.0){
							playerOrientation = 6;
						}else if(angle <= 60.0){
							playerOrientation = 7;
						}else{
							playerOrientation = 0;
						}
					}
				}
				
				// double ylaw = p.getEyeLocation().getYaw();
				double yaw = (p.getLocation().getYaw() - 90) % 360;
				if (yaw < 0) {
					yaw += 360.0;
		        }
				if((337.5 <= yaw && yaw < 360.0) || (0 <= yaw && yaw <=  22.5)){
					// OUEST
					seeOrientation = 6;
				}else if (22.5 <= yaw && yaw < 67.5) {
					// NORD OUEST
					seeOrientation = 7;
		        } else if (67.5 <= yaw && yaw < 112.5) {
		        	// NORD
		        	seeOrientation = 0;
		        } else if (112.5 <= yaw && yaw < 157.5) {
		        	// NORD EST
		        	seeOrientation = 1;
		        } else if (157.5 <= yaw && yaw < 202.5) {
		        	// EST
		        	seeOrientation = 2;
		        } else if (202.5 <= yaw && yaw < 247.5) {
		        	// SUD EST
		        	seeOrientation = 3;
		        } else if (247.5 <= yaw && yaw < 292.5) {
		        	// SUD
		        	seeOrientation = 4;
		        } else if (292.5 <= yaw && yaw < 337.5) {
		        	// SUD OUEST
		        	seeOrientation = 5;
		        }
				
				if(player.getWorld().getName().equals(p.getWorld().getName())){
					int pointOrientation = (playerOrientation - seeOrientation);
					int distance = (int) Math.sqrt((player.getLocation().getX() - p.getLocation().getBlockX())*(player.getLocation().getX() - p.getLocation().getBlockX()) + (player.getLocation().getZ() - p.getLocation().getBlockZ())*(player.getLocation().getZ() - p.getLocation().getBlockZ()) + (player.getLocation().getY() - p.getLocation().getBlockY())*(player.getLocation().getY() - p.getLocation().getBlockY()));
					if(distance <= 100.0){
						orientation = orientation+ChatColor.DARK_GREEN;
					}else if(distance <= 200.0){
						orientation = orientation+ChatColor.YELLOW;
					}else if(distance <= 300.0){
						orientation = orientation+ChatColor.GOLD;
					}else{
						orientation = orientation+ChatColor.RED;
					}
					
					
					switch (pointOrientation){
						case -7:
							orientation = orientation+"⬈ ";
							break;
						case -6:
							orientation = orientation+"➡ ";
							break;
						case -5:
							orientation = orientation+"⬊ ";
							break;
						case -4:
							orientation = orientation+"⬇ ";
							break;
						case -3:
							orientation = orientation+"⬋ ";
							break;
						case -2:
							orientation = orientation+"⬅ ";
							break;
						case -1:
							orientation = orientation+"⬉ ";
							break; 
						case 0:
							orientation = orientation+"⬆ ";
							break;
						case 1:
							orientation = orientation+"⬈ ";
							break;
						case 2:
							orientation = orientation+"➡ ";
							break;
						case 3:
							orientation = orientation+"⬊ ";
							break;
						case 4:
							orientation = orientation+"⬇ ";
							break;
						case 5:
							orientation = orientation+"⬋ ";
							break;
						case 6:
							orientation = orientation+"⬅ ";
							break;
						case 7:
							orientation = orientation+"⬉ ";
							break;                    
					}
					
					orientation = orientation+player.getName()+" "+ChatColor.GRAY+"("+distance+"m)"+ChatColor.RESET+"     ";
				}else{
					orientation = orientation+ChatColor.RED+player.getName()+" "+ChatColor.GRAY+"(?m)"+ChatColor.RESET+"     ";
				}
			}
		}
		return orientation;
	}
}
