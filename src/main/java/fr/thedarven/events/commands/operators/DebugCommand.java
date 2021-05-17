package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.statsgame.RestGame;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class DebugCommand extends OperatorCommand {

	private static String THEDARVEN_UUID = "913198b3-dd6b-47f9-b5eb-6ea2aea0044d";

	public DebugCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		RestGame.endGames();
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (sender.getUniqueId().toString().equals(THEDARVEN_UUID)) {
			return super.validateCommand(sender, pl, cmd, alias, args);
		}
		return false;
	}

}
