package fr.thedarven.events.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import net.md_5.bungee.api.ChatColor;

public class TaupelistCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("taupelist")) {
				if(UtilsClass.molesEnabled()){
					if(!PlayerTaupe.getPlayerManager(p.getUniqueId()).isAlive()) {
						MessagesClass.TaupeListMessage(p);
						
						if(InventoryRegister.supertaupes.getValue() && InventoryRegister.annoncetaupes.getValue()*60+1200 == TaupeGun.timer)
							MessagesClass.SuperTaupeListMessage(p);
					}
				}else{
					p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" "+LanguageBuilder.getContent("COMMAND", "molesNotAnnounced", InventoryRegister.language.getSelectedLanguage(), true));
				}
			}
		}
		return true;
	}

}
