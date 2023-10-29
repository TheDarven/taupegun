package fr.thedarven.events.command.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TCommand extends MoleCommand {

	public TCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.main.getMessageManager().moleSendsMoleMessage(sender, pl, args);
	}

	public boolean canPlayerExecuteCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.canPlayerExecuteCommand(sender, pl, cmd, alias, args)) {
			return args.length > 0;
		}
		return false;
	}

}
