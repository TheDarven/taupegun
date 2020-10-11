package fr.thedarven.events.commands.operators;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import net.md_5.bungee.api.ChatColor;

public class TimerCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("timer") && args.length > 0) {
				if(p.isOp() || p.hasPermission("taupegun.timer")){
					try {
						TaupeGun.timer = Integer.parseInt(args[0]);	
					}catch(NumberFormatException e) {
						p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("COMMAND", "invalidNumber", InventoryRegister.language.getSelectedLanguage(), true));
					}
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}
		}
		return true;
	}

}
