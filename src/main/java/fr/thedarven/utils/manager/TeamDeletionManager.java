package fr.thedarven.utils.manager;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamDeletionManager {

	private TaupeGun main;

	public TeamDeletionManager(TaupeGun main){
		this.main = main;
	}

	public void start() {
		List<TeamCustom> aliveTeams = TeamCustom.getAllAliveTeams();

		if (this.main.getGameManager().getTimer() > this.main.getScenariosManager().molesActivation.getValue()) {
			for (TeamCustom team : aliveTeams) {
				boolean alive = team.isAlive();
				if (team.isTaupeTeam()) {
					alive = PlayerTaupe.getAlivePlayerManager().stream().anyMatch(player -> player.getTaupeTeam() == team && !player.isSuperReveal());
				} else if (team.isSuperTaupeTeam()) {
					alive = PlayerTaupe.getAlivePlayerManager().stream().anyMatch(player -> player.getSuperTaupeTeam() == team);
				} else if (!team.isSpectator() && team.getTeam().getEntries().size() == 0) {
					this.main.getDatabaseManager().updateTeamDeath(team.getTeam().getName());
					alive = false;
					/* Bukkit.broadcastMessage(ChatColor.RED+"L'équipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.RED+" a été éliminée");
					for(Player playerOnline : Bukkit.getOnlinePlayers()) {
						playerOnline.playSound(playerOnline.getLocation(), Sound.ENTITY_GHAST_HURT, 1, 1);
					} */
				}
				team.setAlive(alive);
			}
		}

		aliveTeams = TeamCustom.getAllAliveTeams();

		/* ON REGARDE SI IL NE RESTE QUE UNE EQUIPE */
		if (aliveTeams.size() == 1) {
			TeamCustom team = aliveTeams.get(0);

			Map<String, String> params = new HashMap<>();
			params.put("teamName", "§6" + team.getName() + "§a");
			String teamWinMessage = TextInterpreter.textInterpretation("§a" + LanguageBuilder.getContent("GAME", "teamWin", true), params);

			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(teamWinMessage);
			this.main.getPlayerManager().sendPlaySound(Sound.ENDERDRAGON_DEATH);

			EnumGameState.setState(EnumGameState.END_FIREWORK);
		} else if (aliveTeams.size() == 0) {
			String nobodyWinMessage = "§a" + LanguageBuilder.getContent("GAME", "nobodyWin", true);

			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(nobodyWinMessage);
			this.main.getPlayerManager().sendPlaySound(Sound.ENDERDRAGON_DEATH);

			EnumGameState.setState(EnumGameState.END_FIREWORK);
		}
	}
}
