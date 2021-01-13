package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SuperrevealCommand extends GenericRevealCommand {

	public SuperrevealCommand(TaupeGun main){
		super(main, "cannotSuperReveal", ChatColor.DARK_RED);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.reveal(sender, pl.getSuperTaupeTeam());
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.validateCommand(sender, pl, cmd, alias, args) && UtilsClass.superMolesEnabled() && pl.isSuperTaupe()) {
			if (!pl.isReveal()) {
				sender.sendMessage("§c" + LanguageBuilder.getContent("COMMAND", "cannotSuperReveal", true));
				return false;
			}
			return !pl.isSuperReveal();
		}
		return false;
	}

}
