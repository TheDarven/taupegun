package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SupertCommand extends MoleCommand {

	public SupertCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.main.getMessageManager().superMoleSendsSuperMoleMessage(sender, pl, args);
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		return args.length > 0 && super.validateCommand(sender, pl, cmd, alias, args) && this.main.getGameManager().superMolesEnabled() && pl.isSuperTaupe();
	}

}
