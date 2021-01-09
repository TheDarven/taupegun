package fr.thedarven.events.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.utils.UtilsClass;

public class ScenariosCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("rules") || cmd.getName().equalsIgnoreCase("scenarios"))
				UtilsClass.openConfigInventory(p);
		}
		return true;
	}

}
