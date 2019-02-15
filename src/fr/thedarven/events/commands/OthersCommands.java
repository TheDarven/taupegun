package fr.thedarven.events.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.utils.MessagesClass;

public class OthersCommands implements Listener {

	public OthersCommands(TaupeGun pl) {
	}

	@EventHandler
	public void onCommandes(PlayerCommandPreprocessEvent e){
		String[] args = e.getMessage().split(" ");
		Player p = e.getPlayer();
		/* if(args[0].equalsIgnoreCase("/timer") && TaupeGun.developpement && args.length >= 2){
			e.setCancelled(true);
			if(p.isOp()){
				TaupeGun.timer = Integer.parseInt(args[1]);
			}else{
				MessagesClass.CannotCommandOperatorMessage(p);
			}	
		} */
	}
}
