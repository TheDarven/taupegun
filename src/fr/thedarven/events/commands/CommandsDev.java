package fr.thedarven.events.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandsDev implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		// if(sender instanceof Player){
			// Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("test")) {
				if(args.length > 0) {
					
				}
			}
		// }
		return true;
	}
	
}