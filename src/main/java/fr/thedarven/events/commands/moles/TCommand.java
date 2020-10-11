package fr.thedarven.events.commands.moles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.messages.MessagesClass;

public class TCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
			if(UtilsClass.molesEnabled() && pc.isTaupe() && pc.isAlive()){
				if(cmd.getName().equalsIgnoreCase("t") && args.length > 0){
					MessagesClass.CommandTaupeMessageMessage(p, args, pc.getTaupeTeam());
				}
			}
		}
		return true;
	}

}
