package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.messages.MessagesClass;

public class PlayerkillCommand implements CommandExecutor{

	private TaupeGun main;

	public PlayerkillCommand(TaupeGun main){
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("playerkill") && args.length >= 1 && EnumGameState.isCurrentState(EnumGameState.GAME) && UtilsClass.playerInGame(args[0]) != null){
				if(p.isOp() || p.hasPermission("taupegun.playerkill")) {
					PlayerTaupe pl = PlayerTaupe.getPlayerManager(UtilsClass.playerInGame(args[0]));
					if(!pl.isAlive())
						return false;

					if(pl.isOnline())
						pl.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1000, 250));
					else
						this.main.getEventsManager().getDeath().killPlayer(pl, true);
				}else {
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}
		}
		return false;
	}
	
}
