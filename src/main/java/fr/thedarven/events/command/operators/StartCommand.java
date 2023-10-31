package fr.thedarven.events.command.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.runnable.StartRunnable;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.team.graph.MoleCreationGraph;
import fr.thedarven.team.graph.MoleCreationSuccessEnum;
import fr.thedarven.utils.helpers.PermissionHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class StartCommand extends OperatorCommand {

	private StartRunnable startRunnable;

	public StartCommand(TaupeGun main){
		super(main, new String[]{PermissionHelper.START_COMMAND});
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		MoleCreationGraph graph = this.main.getScenariosManager().moleTeamMate.getMoleCreationGraph();

		MoleCreationSuccessEnum moleTeamCreationSuccess = graph.createTeams();
		if (moleTeamCreationSuccess == MoleCreationSuccessEnum.INCORRECT_MOLE_AMOUNT) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "incorrectMoleNumber", true));
			TeamCustom.deleteTeamTaupe();
			for(PlayerTaupe playerTaupe : PlayerTaupe.getAllPlayerManager()) {
				playerTaupe.setTaupeTeam(null);
				playerTaupe.setSuperTaupeTeam(null);
			}
			return;
		}

		sender.sendMessage("§9" + LanguageBuilder.getContent("START_COMMAND", "gameCanStart", true));
		EnumGameState.setState(EnumGameState.WAIT);

		this.startRunnable = new StartRunnable(this.main);
		this.startRunnable.runTaskTimer(this.main,20,20);
	}

	public boolean canPlayerExecuteCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (!super.canPlayerExecuteCommand(sender, pl, cmd, alias, args)) {
			return false;
		}

		if (!EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "gameAlreadyStarted", true));
			return false;
		}

		if (Objects.isNull(this.main.getWorldManager().getWorld())) {
			String worldNotExistMessage = "§e" + LanguageBuilder.getContent("START_COMMAND", "worldNotExist", true);
			Bukkit.broadcastMessage(worldNotExistMessage);
			return false;
		}

		List<TeamCustom> teams = TeamCustom.getAllStartTeams();
		if (teams.size() < 2 && !this.main.getScenariosManager().superMoles.getValue()) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "needTwoTeams", true));
			return false;
		}

		if (teams.size() < 3 && this.main.getScenariosManager().superMoles.getValue()) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "needThreeTeams", true));
			return false;
		}

		if (this.main.getKitManager().countKits() < 1) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "notEnoughKits", true));
			return false;
		}

		for (TeamCustom team: teams) {
			if (notEnoughPlayersPerTeam(team)) {
				sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "notEnoughPlayersPerTeam", true));
				return false;
			}
			if (team.getConnectedPlayers().size() > team.getSize()) {
				sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "disconnectedPlayer", true));
				return false;
			}
		}

		return true;
	}

	private boolean notEnoughPlayersPerTeam(TeamCustom team) {
		return (this.main.getScenariosManager().numberOfMole.getValue() == 1 && team.getSize() == 2) || this.main.getScenariosManager().numberOfMole.getValue() == 2 && team.getSize() < 4;
	}

	public void stopStartRunnable() {
		if (Objects.nonNull(this.startRunnable)) {
			this.startRunnable.cancel();
			this.startRunnable = null;
		}
	}
}
