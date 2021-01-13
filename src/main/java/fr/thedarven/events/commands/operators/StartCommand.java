package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.runnable.StartRunnable;
import fr.thedarven.models.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.teams.TeamGraph;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class StartCommand extends OperatorCommand {

	private StartRunnable startRunnable;

	public StartCommand(TaupeGun main){
		super(main, new String[]{ "taupegun.start" });
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		TeamGraph graph = new TeamGraph(this.main);

		Random r = new Random();

		List<TeamCustom> teams = TeamCustom.getAllStartTeams();
		for (TeamCustom team: teams) {
			List<PlayerTaupe> moles = new ArrayList<>();
			List<PlayerTaupe> playerList = new ArrayList<>(team.getPlayers());

			if (team.getSize() == 1 || team.getSize() == 3 || (team.getSize() > 3 && this.main.getInventoryRegister().nombretaupes.getValue() == 1)) {
				moles.add(playerList.get(r.nextInt(team.getSize())));
				graph.addEquipes(moles);
			} else if (team.getSize() > 3 && this.main.getInventoryRegister().nombretaupes.getValue() == 2) {
				int taupeInt1 = r.nextInt(team.getSize());
				int taupeInt2 = r.nextInt(team.getSize());
				while (taupeInt1 == taupeInt2) {
					taupeInt2 = r.nextInt(team.getSize());
				}
				moles.add(playerList.get(taupeInt1));
				moles.add(playerList.get(taupeInt2));
				graph.addEquipes(moles);
			}
		}

		boolean successTeamCreation = graph.creationEquipes();
		if (!successTeamCreation) {
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

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (!super.validateCommand(sender, pl, cmd, alias, args))
			return false;

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
		if (teams.size() < 2 && !this.main.getInventoryRegister().supertaupes.getValue()) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "needTwoTeams", true));
			return false;
		}

		if (teams.size() < 3 && this.main.getInventoryRegister().supertaupes.getValue()) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("START_COMMAND", "needThreeTeams", true));
			return false;
		}

		if (this.main.getInventoryRegister().kits.getChilds().size() <= 1) {
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
		return (this.main.getInventoryRegister().nombretaupes.getValue() == 1 && team.getSize() == 2) || this.main.getInventoryRegister().nombretaupes.getValue() == 2 && team.getSize() < 4;
	}

	public void stopStartRunnable() {
		if (!Objects.isNull(this.startRunnable)) {
			this.startRunnable.cancel();
			this.startRunnable = null;
		}
	}
}
