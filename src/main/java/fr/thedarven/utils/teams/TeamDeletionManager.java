package fr.thedarven.utils.teams;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class TeamDeletionManager {

	private TaupeGun main;

	public TeamDeletionManager(TaupeGun main){
		this.main = main;
	}

	public void start() {
		if(EnumGameState.isCurrentState(EnumGameState.GAME)) {
			for(TeamCustom team : TeamCustom.getAllAliveTeams()) {
				if(this.main.getGameManager().getTimer() > this.main.getInventoryRegister().molesActivation.getValue()) {
					if(team.isTaupeTeam()) {
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
							if(player.getSuperTaupeTeam() == team)
								deleteTeamSuperTaupe = false;
						}
						if(deleteTeamSuperTaupe) {
							team.setAlive(false);
						}
					}else if(!team.isSpectator() && team.getTeam().getEntries().size() == 0) {
						this.main.getDatabaseManager().updateTeamDeath(team.getTeam().getName());
						team.setAlive(false);
						/* Bukkit.broadcastMessage(ChatColor.RED+"L'équipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.RED+" a été éliminée");
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.ENTITY_GHAST_HURT, 1, 1);
						} */
					}
				}
			}
			
			/* ON REGARDE SI IL NE RESTE QUE UNE EQUIPE */
			if(TeamCustom.getAllAliveTeams().size() == 1){
				TeamCustom team = TeamCustom.getAllAliveTeams().get(0);
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("teamName", "§6"+team.getTeam().getName()+"§a");
				String teamWinMessage = TextInterpreter.textInterpretation("§a"+LanguageBuilder.getContent("GAME", "teamWin", true), params);
				
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(teamWinMessage);
				for(Player playerOnline : Bukkit.getOnlinePlayers())
					playerOnline.playSound(playerOnline.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
				EnumGameState.setState(EnumGameState.END_FIREWORK);
			}else if(TeamCustom.getAllAliveTeams().size() == 0){
				String nobodyWinMessage = "§a"+LanguageBuilder.getContent("GAME", "nobodyWin", true);
				
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(nobodyWinMessage);
				for(Player playerOnline : Bukkit.getOnlinePlayers())
					playerOnline.playSound(playerOnline.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
				EnumGameState.setState(EnumGameState.END_FIREWORK);
			}
		}
	}
}
