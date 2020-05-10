package fr.thedarven.events.commands.moles;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.teams.TeamDelete;
import fr.thedarven.utils.texts.TextInterpreter;

public class RevealCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
			if(UtilsClass.molesEnabled() && pc.isTaupe() && pc.isAlive()){
				if(cmd.getName().equalsIgnoreCase("reveal")) {
					if(pc.revealTaupe()){
						pc.getTaupeTeam().joinTeam(pc.getUuid());
						p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.GOLDEN_APPLE,1));
						if(p.getHealth() < 16.0){
							p.setHealth(p.getHealth()+4.0);
						}else{
							p.setHealth(20.0);
						}
						
						Map<String, String> params = new HashMap<String, String>();
						params.put("playerName", p.getName());
						String revealMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("COMMAND", "reveal", InventoryRegister.language.getSelectedLanguage(), true), params);

						Bukkit.broadcastMessage(ChatColor.RED+revealMessage);
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
						}
						
						TeamDelete.start();
					}
				}
			}
		}
		return true;
	}

}
