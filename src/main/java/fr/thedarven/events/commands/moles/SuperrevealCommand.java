package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;

public class SuperrevealCommand extends GenericRevealCommand{

	public SuperrevealCommand(TaupeGun main){
		super(main, "cannotSuperReveal", ChatColor.DARK_RED);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(!(sender instanceof Player))
			return true;

		Player p = (Player) sender;
		PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
		if(!UtilsClass.superMolesEnabled() || !pc.isSuperTaupe() || !pc.isAlive() || !cmd.getName().equalsIgnoreCase("superreveal"))
			return true;

		if(!pc.isReveal()){
			p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("COMMAND", "cannotSuperReveal", InventoryRegister.language.getSelectedLanguage(), true));
			return true;
		}

		if(!pc.revealSuperTaupe())
			return true;

		this.reveal(p, pc.getSuperTaupeTeam());

		return true;
	}

}
