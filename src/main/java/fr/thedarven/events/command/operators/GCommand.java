package fr.thedarven.events.command.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.utils.helpers.PermissionHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class GCommand extends OperatorCommand {

	public GCommand(TaupeGun main){
		super(main, new String[]{PermissionHelper.G_COMMAND});
	}

	@Override
	public void executeCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		StringBuilder message = new StringBuilder(" ");
		for (String word: args) {
			message.append(word).append(" ");
		}
		String infoMessage = LanguageBuilder.getContent("CONTENT", "info", true);
		Bukkit.broadcastMessage("§e" + infoMessage + "§a" + message);
	}

	public boolean canPlayerExecuteCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (args.length > 0) {
			return super.canPlayerExecuteCommand(sender, pl, cmd, alias, args);
		}
		return false;
	}
}
