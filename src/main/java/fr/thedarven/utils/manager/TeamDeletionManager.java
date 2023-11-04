package fr.thedarven.utils.manager;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamDeletionManager {

	private final TaupeGun main;

	public TeamDeletionManager(TaupeGun main){
		this.main = main;
	}

	public void start() {
		List<TeamCustom> livingTeams = this.main.getTeamManager().getAllLivingTeams();

		if (this.main.getGameManager().getTimer() > this.main.getScenariosManager().molesActivation.getValue()) {
			for (TeamCustom team : livingTeams) {
				boolean alive = team.isAlive();
				if (team.isMoleTeam()) {
					alive = PlayerTaupe.getAlivePlayerManager().stream().anyMatch(player -> player.getTaupeTeam() == team && !player.isSuperReveal());
				} else if (team.isSuperMoleTeam()) {
					alive = PlayerTaupe.getAlivePlayerManager().stream().anyMatch(player -> player.getSuperTaupeTeam() == team);
				} else if (!team.isSpectator() && team.countMembers() == 0) {
					this.main.getDatabaseManager().updateTeamDeath(team.getName(), true);
					alive = false;
				}
				team.setAlive(alive);
			}
		}

		livingTeams = this.main.getTeamManager().getAllLivingTeams();

		/* ON REGARDE SI IL NE RESTE QUE UNE EQUIPE */
		if (livingTeams.size() == 1) {
			TeamCustom team = livingTeams.get(0);

			Map<String, String> params = new HashMap<>();
			params.put("teamName", "§6" + team.getName() + "§a");
			String teamWinMessage = TextInterpreter.textInterpretation("§a" + LanguageBuilder.getContent("GAME", "teamWin", true), params);

			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(teamWinMessage);
			Bukkit.broadcastMessage(" ");
			this.main.getPlayerManager().sendPlaySound(Sound.ENDERDRAGON_DEATH);

			EnumGameState.setState(EnumGameState.END_FIREWORK);
		} else if (livingTeams.isEmpty()) {
			String nobodyWinMessage = "§a" + LanguageBuilder.getContent("GAME", "nobodyWin", true);

			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(nobodyWinMessage);
			Bukkit.broadcastMessage(" ");
			this.main.getPlayerManager().sendPlaySound(Sound.ENDERDRAGON_DEATH);

			EnumGameState.setState(EnumGameState.END_FIREWORK);
		}
	}
}
