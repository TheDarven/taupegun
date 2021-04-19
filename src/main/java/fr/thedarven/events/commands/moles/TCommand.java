package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.utils.messages.MessagesClass;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TCommand extends MoleCommand {

	public TCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		MessagesClass.CommandTaupeMessageMessage(sender, args, pl.getTaupeTeam());
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.validateCommand(sender, pl, cmd, alias, args)) {
			return args.length > 0;
		}
		return false;
	}

}
