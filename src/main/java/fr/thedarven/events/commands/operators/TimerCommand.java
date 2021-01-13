package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TimerCommand extends OperatorCommand {

	public TimerCommand(TaupeGun main){
		super(main, new String[]{ "taupegun.timer" });
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		try {
			this.main.getGameManager().setTimer(Integer.parseInt(args[0]));
		} catch(NumberFormatException e) {
			sender.sendMessage("§c" + LanguageBuilder.getContent("COMMAND", "invalidNumber", true));
		}
	}

}
