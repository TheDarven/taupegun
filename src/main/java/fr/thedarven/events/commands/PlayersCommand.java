package fr.thedarven.events.commands;

import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class PlayersCommand extends PlayerCommand {

	public PlayersCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		StringBuilder message = new StringBuilder("§e");
		message.append(LanguageBuilder.getContent("COMMAND", "playerList", true));

		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
			for (PlayerTaupe playerTaupe : PlayerTaupe.getAllPlayerManager()) {
				message.append(playerTaupe.isOnline() ? "§a" : "§c").append(playerTaupe.getName()).append(" §l§f︱ §r");
			}
		} else {
			for(PlayerTaupe playerTaupe : PlayerTaupe.getAlivePlayerManager()) {
				message.append(playerTaupe.isOnline() ? "§a" : "§c").append(playerTaupe.getName()).append(" §l§f︱ §r");
			}
		}
		sender.sendMessage(message.toString());
	}

}
