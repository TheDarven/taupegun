package fr.thedarven.events.command;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ScenariosCommand extends PlayerCommand {

	public ScenariosCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.main.getPlayerManager().openConfigInventory(sender);
	}

}
