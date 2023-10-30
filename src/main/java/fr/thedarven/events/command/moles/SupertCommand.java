package fr.thedarven.events.command.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SupertCommand extends MoleCommand {

	public SupertCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.main.getMessageManager().superMoleSendsSuperMoleMessage(sender, pl, args);
	}

	public boolean canPlayerExecuteCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		return args.length > 0 && super.canPlayerExecuteCommand(sender, pl, cmd, alias, args) && this.main.getGameManager().areSuperMolesRevealed() && pl.isSuperTaupe();
	}

}
