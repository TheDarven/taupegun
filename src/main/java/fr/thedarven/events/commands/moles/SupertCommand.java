package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.messages.MessagesClass;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SupertCommand extends MoleCommand {

	public SupertCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		MessagesClass.CommandSupertaupeMessageMessage(sender, args, pl.getSuperTaupeTeam());
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		return args.length > 0 && super.validateCommand(sender, pl, cmd, alias, args) && UtilsClass.superMolesEnabled() && pl.isSuperTaupe();
	}

}
