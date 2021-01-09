package fr.thedarven.events.commands.operators;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.TaupeGun;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import net.md_5.bungee.api.ChatColor;

public class TimerCommand implements CommandExecutor{

	private TaupeGun main;

	public TimerCommand(TaupeGun main){
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("timer") && args.length > 0) {
				if(p.isOp() || p.hasPermission("taupegun.timer")){
					try {
						this.main.getGameManager().setTimer(Integer.parseInt(args[0]));
					}catch(NumberFormatException e) {
						p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("COMMAND", "invalidNumber", true));
					}
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}
		}
		return true;
	}

}
