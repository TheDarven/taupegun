package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.utils.PermissionHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TimerCommand extends OperatorCommand {

	public TimerCommand(TaupeGun main){
		super(main, new String[]{PermissionHelper.TIMER_COMMAND});
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		try {
			this.main.getGameManager().setTimer(Integer.parseInt(args[0]));
		} catch(NumberFormatException e) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("COMMAND", "invalidNumber", true));
		}
	}

	@Override
	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		return this.main.development && super.validateCommand(sender, pl, cmd, alias, args);
	}
}
