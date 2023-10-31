package fr.thedarven.events.command;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ScenariosCommand extends PlayerCommand {

	public ScenariosCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.main.getPlayerManager().openConfigInventory(sender);
	}

}
