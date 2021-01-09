package fr.thedarven.events.commands;

import fr.thedarven.TaupeGun;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.listeners.ScenariosItemInteract;
import fr.thedarven.main.metier.EnumGameState;

public class ItemCommand implements CommandExecutor {

	private TaupeGun main;

	public ItemCommand(TaupeGun main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("item")) {
				if(EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
					ScenariosItemInteract.actionBeacon(p);
					this.main.getInventoryRegister().ownteam.actionBanner(p);
				}
			}
		}
		return true;
	}
}
