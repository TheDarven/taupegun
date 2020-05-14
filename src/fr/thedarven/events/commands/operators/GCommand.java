package fr.thedarven.events.commands.operators;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;

public class GCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("g")) {
				if(p.isOp() || p.hasPermission("taupegun.g")){
					if(args.length > 0) {
						String message = " ";
						for(int i=0; i<args.length; i++){
							message = message+args[i]+" ";
						}
						String infoMessage = LanguageBuilder.getContent("CONTENT", "info", InventoryRegister.language.getSelectedLanguage(), true);
						Bukkit.broadcastMessage("§e"+infoMessage+"§a"+message);	
					}
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}	
			}
		}
		return true;
	}
}
