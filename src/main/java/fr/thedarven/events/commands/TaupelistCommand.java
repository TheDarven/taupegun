package fr.thedarven.events.commands;

import fr.thedarven.TaupeGun;
import fr.thedarven.messages.MessageManager;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TaupelistCommand extends PlayerCommand {

	public TaupelistCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		MessageManager messageManager = this.main.getMessageManager();

		messageManager.sendTaupeListMessage(sender);
		if (this.main.getScenariosManager().superMoles.getValue()) {
			messageManager.sendSuperTaupeListMessage(sender);
		}
	}

	public boolean canPlayerExecuteCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.canPlayerExecuteCommand(sender, pl, cmd, alias, args)) {
			if (!this.main.getScenariosManager().taupelistCommand.getValue()) {
				sender.sendMessage("§c" + LanguageBuilder.getContent("COMMAND", "disabledCommand", true));
				return false;
			}

			if (!this.main.getGameManager().molesEnabled()) {
				sender.sendMessage("§c" + LanguageBuilder.getContent("COMMAND", "molesNotAnnounced", true));
				return false;
			}

			return !pl.isAlive();
		}
		return false;
	}
}
