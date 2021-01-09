package fr.thedarven.events.commands;

import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.PlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import fr.thedarven.events.listeners.ScenariosItemInteract;
import fr.thedarven.main.metier.EnumGameState;

public class ItemCommand extends PlayerCommand {

	public ItemCommand(TaupeGun main) {
		super(main, new EnumGameState[]{ EnumGameState.LOBBY });
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		ScenariosItemInteract.actionBeacon(sender);
		this.main.getInventoryRegister().ownteam.actionBanner(sender);
	}

}
