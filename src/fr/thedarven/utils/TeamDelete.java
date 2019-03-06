package fr.thedarven.utils;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;

public class TeamDelete {

	public static void start() {
		if(TaupeGun.etat.equals(EnumGame.GAME)) {
			ArrayList<String> deleteTeam = new ArrayList<String>();
			Set<Team> teams = Teams.board.getTeams();
			for(Team team : teams) {
				if(TaupeGun.timer > InventoryRegister.annoncetaupes.getValue()*60) {
					if(team.getName().startsWith("Taupes")) {
						int number = Integer.parseInt(team.getName().substring(6));
						boolean deleteTeamTaupe1 = true;
						for(PlayerTaupe player : PlayerTaupe.getAlivePlayerManager()) {
							if(player.getTaupeTeam() == number && !player.isSuperReveal()) {
								deleteTeamTaupe1 = false;
							}
						}
						if(deleteTeamTaupe1) {
							Teams.deleteTeam("Taupes"+number);
						}
					}else if(team.getName().startsWith("SuperTaupe")) {
						boolean deleteTeamSuperTaupe = true;
						for(PlayerTaupe player : PlayerTaupe.getAlivePlayerManager()) {
							if(player.getSuperTaupeTeam() == 1) {
								deleteTeamSuperTaupe = false;
							}
						}
						if(deleteTeamSuperTaupe) {
							Teams.deleteTeam("SuperTaupe");
						}
					}else if(team.getName() != "Spectateurs" && team.getEntries().size() == 0) {
						deleteTeam.add(team.getName());
					}
				}
			}
			
			for(String teamName : deleteTeam) {
				SqlRequest.updateTeamMort(teamName);
				Teams.deleteTeam(teamName);
				/* Bukkit.broadcastMessage(ChatColor.RED+"L'�quipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.RED+" a �t� �limin�e");
				for(Player playerOnline : Bukkit.getOnlinePlayers()) {
					playerOnline.playSound(playerOnline.getLocation(), Sound.ENTITY_GHAST_HURT, 1, 1);
				} */
			}
			
			/* ON REGARDE SI IL NE RESTE QUE UNE EQUIPE */
			teams = Teams.board.getTeams();
			if(teams.size() == 2){
				for(Team team : teams){
					if(team.getName() != "Spectateurs"){
						Bukkit.broadcastMessage(" ");
						Bukkit.broadcastMessage(ChatColor.GREEN+"L'�quipe "+ChatColor.GOLD+team.getName()+ChatColor.GREEN+" a gagn� !");
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
						}
						TaupeGun.etat = EnumGame.END_FIREWORK;
					}
				}
			}	
		}
	}
}
