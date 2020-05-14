package fr.thedarven.events.commands.operators;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.utils.messages.MessagesClass;

public class SayCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("say")) {
				if(p.isOp() || p.hasPermission("taupegun.say")){
					if(args.length > 0) {
						String message = " ";
						for(int i=0; i<args.length; i++){
							message = message+args[i]+" ";
						}
						message = "§l︳ §e"+p.getName()+" §r§7≫§a"+message;
						Bukkit.broadcastMessage(" \n"+message+"\n ");	
					}
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}
		}
		return true;
	}
}
