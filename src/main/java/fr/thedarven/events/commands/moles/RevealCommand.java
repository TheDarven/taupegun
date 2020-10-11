package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;

public class RevealCommand extends GenericRevealCommand{

	public RevealCommand(TaupeGun main){
		super(main, "reveal", ChatColor.RED);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(!(sender instanceof Player))
			return true;

		Player p = (Player) sender;
		PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
		if(!UtilsClass.molesEnabled() || !pc.isTaupe() || !pc.isAlive() || !cmd.getName().equalsIgnoreCase("reveal"))
			return true;

		if(!pc.revealTaupe())
			return true;

		this.reveal(p, pc.getTaupeTeam());

		return true;
	}

}
