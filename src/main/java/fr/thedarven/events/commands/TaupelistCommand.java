package fr.thedarven.events.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;

public class TaupelistCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("taupelist")) {
				if(InventoryRegister.taupelistCommand.getValue()) {
					if(UtilsClass.molesEnabled()){
						if(!PlayerTaupe.getPlayerManager(p.getUniqueId()).isAlive()) {
							MessagesClass.TaupeListMessage(p);
							
							if(InventoryRegister.supertaupes.getValue())
								MessagesClass.SuperTaupeListMessage(p);
						}
					}else
						p.sendMessage("§c"+LanguageBuilder.getContent("COMMAND", "molesNotAnnounced", InventoryRegister.language.getSelectedLanguage(), true));
				}else
					p.sendMessage("§c"+LanguageBuilder.getContent("COMMAND", "disabledCommand", InventoryRegister.language.getSelectedLanguage(), true));
			}
		}
		return true;
	}
}
