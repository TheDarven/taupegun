package fr.thedarven.events.commands;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import fr.thedarven.models.enums.EnumGameState;

public class ItemCommand extends PlayerCommand {

	public ItemCommand(TaupeGun main) {
		super(main, new EnumGameState[]{ EnumGameState.LOBBY });
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.main.getInventoryRegister().scenariosvisibles.reloadScenariosItem(sender);
		this.main.getInventoryRegister().ownteam.actionBanner(sender);
	}

}
