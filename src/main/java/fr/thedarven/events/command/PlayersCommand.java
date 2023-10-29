package fr.thedarven.events.command;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.EnumGameState;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class PlayersCommand extends PlayerCommand {

	public PlayersCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		StringBuilder message = new StringBuilder("§e");
		message.append(LanguageBuilder.getContent("COMMAND", "playerList", true));

		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
			for (StatsPlayerTaupe playerTaupe : StatsPlayerTaupe.getAllPlayerManager()) {
				message.append(playerTaupe.isOnline() ? "§a" : "§c").append(playerTaupe.getName()).append(" §l§f︱ §r");
			}
		} else {
			for(StatsPlayerTaupe playerTaupe : StatsPlayerTaupe.getAlivePlayerManager()) {
				message.append(playerTaupe.isOnline() ? "§a" : "§c").append(playerTaupe.getName()).append(" §l§f︱ §r");
			}
		}
		sender.sendMessage(message.toString());
	}

}
