package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class RevealCommand extends GenericRevealCommand{

	public RevealCommand(TaupeGun main){
		super(main, "reveal", ChatColor.RED);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.reveal(sender, pl.getTaupeTeam());
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.validateCommand(sender, pl, cmd, alias, args)) {
			return !pl.isReveal();
		}
		return false;
	}

}
