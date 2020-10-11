package fr.thedarven.events.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;

public class PlayersCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("players")){
				String message = "§e"+LanguageBuilder.getContent("COMMAND", "playerList", InventoryRegister.language.getSelectedLanguage(), true);
				if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
					for(PlayerTaupe player : PlayerTaupe.getAllPlayerManager()) {
						if(player.isOnline()) {
							message+="§a";
						}else {
							message+="§c";
						}
						message+=player.getName()+" §l§f︱ §r";
					}
				}else {
					for(PlayerTaupe player : PlayerTaupe.getAlivePlayerManager()) {
						if(player.isOnline()) {
							message+="§a";
						}else {
							message+="§c";
						}
						message+=player.getName()+" §l§f︱ §r";
					}	
				}
				p.sendMessage(message);
			}
		}
		return true;
	}

}
