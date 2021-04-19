package fr.thedarven.events.commands;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import fr.thedarven.models.enums.EnumGameState;

public class ItemCommand extends PlayerCommand {

	public ItemCommand(TaupeGun main) {
		super(main, new EnumGameState[]{ EnumGameState.LOBBY });
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		this.main.getScenariosManager().scenariosVisible.reloadScenariosItem(sender);
		this.main.getScenariosManager().ownTeam.actionBanner(sender);
	}

}
