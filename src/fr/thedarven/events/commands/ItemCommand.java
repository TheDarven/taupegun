package fr.thedarven.events.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.ScenariosItemInteract;
import fr.thedarven.main.metier.EnumGameState;

public class ItemCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("item")) {
				if(EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
					ScenariosItemInteract.actionBeacon(p);
					InventoryRegister.ownteam.actionBanner(p);
				}
			}
		}
		return true;
	}
}
