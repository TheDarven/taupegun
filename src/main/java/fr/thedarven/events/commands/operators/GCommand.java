package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.utils.PermissionHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class GCommand extends OperatorCommand {

	public GCommand(TaupeGun main){
		super(main, new String[]{PermissionHelper.G_COMMAND});
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		StringBuilder message = new StringBuilder(" ");
		for (String word: args) {
			message.append(word).append(" ");
		}
		String infoMessage = LanguageBuilder.getContent("CONTENT", "info", true);
		Bukkit.broadcastMessage("§e" + infoMessage + "§a" + message);
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (args.length > 0) {
			return super.validateCommand(sender, pl, cmd, alias, args);
		}
		return false;
	}
}
