package fr.thedarven.events.command.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
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

	public boolean canPlayerExecuteCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.canPlayerExecuteCommand(sender, pl, cmd, alias, args)) {
			return !pl.isReveal();
		}
		return false;
	}

}
