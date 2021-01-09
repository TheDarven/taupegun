package fr.thedarven.events.commands;

import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ScenariosCommand extends PlayerCommand {

	public ScenariosCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		UtilsClass.openConfigInventory(sender);
	}

}
