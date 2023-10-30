package fr.thedarven.events.command.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.utils.helpers.PermissionHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SayCommand implements CommandExecutor {

	private TaupeGun main;

	public SayCommand(TaupeGun main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (args.length == 0)
			return true;

		String senderName = "CONSOLE";

		if (sender instanceof Player){
			Player player = (Player) sender;

			if (!PermissionHelper.canPlayerUseSayCommand(player)){
				this.main.getMessageManager().sendNotOperatorMessage(player);
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
