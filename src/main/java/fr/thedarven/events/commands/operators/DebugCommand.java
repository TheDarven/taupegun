package fr.thedarven.events.commands.operators;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.statsgame.RestGame;

public class DebugCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.getUniqueId().toString().equals("913198b3-dd6b-47f9-b5eb-6ea2aea0044d")) {
				RestGame.endGames();
			}
		}
		return true;
	}

}
