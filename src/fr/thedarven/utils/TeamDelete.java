package fr.thedarven.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;

public class TeamDelete {

	public static void start() {
		if(TaupeGun.etat.equals(EnumGame.GAME)) {
			for(TeamCustom team : TeamCustom.getAllAliveTeams()) {
				if(TaupeGun.timer > InventoryRegister.annoncetaupes.getValue()*60) {
					if(team.isTaupeTeam()) {
						// int number = Integer.parseInt(team.getName().substring(6));
						boolean deleteTeamTaupe1 = true;
						for(PlayerTaupe player : PlayerTaupe.getAlivePlayerManager()) {
							if(player.getTaupeTeam() == team && !player.isSuperReveal()) {
								deleteTeamTaupe1 = false;
							}
						}
						if(deleteTeamTaupe1) {
							team.setAlive(false);
						}
					}else if(team.isSuperTaupeTeam()) {
						boolean deleteTeamSuperTaupe = true;
						for(PlayerTaupe player : PlayerTaupe.getAlivePlayerManager()) {
							if(player.getSuperTaupeTeam() == team) {
								deleteTeamSuperTaupe = false;
							}
						}
						if(deleteTeamSuperTaupe) {
							team.setAlive(false);
						}
					}else if(!team.getSpectator() && team.getTeam().getEntries().size() == 0) {
						SqlRequest.updateTeamMort(team.getTeam().getName());
						team.setAlive(false);
						/* Bukkit.broadcastMessage(ChatColor.RED+"L'�quipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.RED+" a �t� �limin�e");
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.ENTITY_GHAST_HURT, 1, 1);
						} */
					}
				}
			}
			
			/* ON REGARDE SI IL NE RESTE QUE UNE EQUIPE */
			
			if(TeamCustom.getAllAliveTeams().size() == 1){
				TeamCustom team = TeamCustom.getAllAliveTeams().get(0);
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(ChatColor.GREEN+"L'�quipe "+ChatColor.GOLD+team.getTeam().getName()+ChatColor.GREEN+" a gagn� !");
				for(Player playerOnline : Bukkit.getOnlinePlayers()) {
					playerOnline.playSound(playerOnline.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
				}
				TaupeGun.etat = EnumGame.END_FIREWORK;
			}else if(TeamCustom.getAllAliveTeams().size() == 0){
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(ChatColor.GREEN+"Personne n'a gagn� !");
				for(Player playerOnline : Bukkit.getOnlinePlayers()) {
					playerOnline.playSound(playerOnline.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
				}
				TaupeGun.etat = EnumGame.END_FIREWORK;
			}
		}
	}
}
