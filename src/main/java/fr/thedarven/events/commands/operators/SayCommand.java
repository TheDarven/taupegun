package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.utils.messages.MessagesClass;

import java.util.Arrays;

public class SayCommand implements CommandExecutor {

	private TaupeGun main;

	public SayCommand(TaupeGun main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(args.length == 0)
			return true;

		String senderName = "CONSOLE";

		if (sender instanceof Player){
			Player player = (Player) sender;

			if (!player.isOp() && !player.hasPermission("taupegun.say")){
				MessagesClass.CannotCommandOperatorMessage(player);
				return true;
			}

			senderName = player.getName();
		}

		StringBuilder content = new StringBuilder(" ");
		StringBuilder builder = new StringBuilder();

		Arrays.stream(args).forEach(arg -> content.append(arg).append(" "));
		builder.append(" \n§l︳ §e").append(senderName).append(" §r§7≫§a").append(content).append("\n ");

		Bukkit.broadcastMessage(builder.toString());
		return true;
	}
}
