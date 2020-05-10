package fr.thedarven.events.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumGameState;

public class ScenariosCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("rules") || cmd.getName().equalsIgnoreCase("scenarios")){
				if((p.isOp() || p.hasPermission("taupegun.scenarios")) && EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
					p.openInventory(InventoryRegister.menu.getInventory());
				}else if(InventoryRegister.scenariosvisibles.getValue()) {
					p.openInventory(InventoryRegister.configuration.getInventory());
				}
			}
		}
		return true;
	}

}
