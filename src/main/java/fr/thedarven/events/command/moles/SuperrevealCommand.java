package fr.thedarven.events.command.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SuperrevealCommand extends GenericRevealCommand {

	public SuperrevealCommand(TaupeGun main){
		super(main, "superReveal", ChatColor.DARK_RED);
	}

	@Override
	public void executeCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.reveal(sender, pl.getSuperTaupeTeam());
	}

	public boolean canPlayerExecuteCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.canPlayerExecuteCommand(sender, pl, cmd, alias, args) && this.main.getGameManager().areSuperMolesRevealed() && pl.isSuperTaupe()) {
			if (!pl.isReveal()) {
				sender.sendMessage("§c" + LanguageBuilder.getContent("COMMAND", "cannotSuperReveal", true));
				return false;
			}
			return !pl.isSuperReveal();
		}
		return false;
	}

}
