package fr.thedarven.events.command;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import fr.thedarven.model.enums.EnumGameState;

public class ItemCommand extends PlayerCommand {

	public ItemCommand(TaupeGun main) {
		super(main, new EnumGameState[]{ EnumGameState.LOBBY });
	}

	@Override
	public void executeCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.main.getScenariosManager().reloadPlayerItemOfPlayer(sender);
	}

}
