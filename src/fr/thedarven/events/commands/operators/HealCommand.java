package fr.thedarven.events.commands.operators;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;

public class HealCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("heal") && EnumGameState.isCurrentState(EnumGameState.GAME)) {
				if(p.isOp() || p.hasPermission("taupegun.heal")){
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 100, true, false));
					}
					Bukkit.broadcastMessage("§6[TaupeGun] §a"+LanguageBuilder.getContent("COMMAND", "heal", InventoryRegister.language.getSelectedLanguage(), true));
				}else {
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}
		}
		return true;
	}
}
