package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.kits.InventoryKitsElement;
import fr.thedarven.players.PlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ClaimCommand extends MoleCommand {

	public ClaimCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		InventoryKitsElement inventoryKitsElement = InventoryKitsElement.getInventoryKitElement(pl.getClaimTaupe());
		if (Objects.nonNull(inventoryKitsElement)) {
			inventoryKitsElement.giveItems(sender);
			pl.setClaimTaupe("aucun");
		}
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.validateCommand(sender, pl, cmd, alias, args)) {
			return !pl.getClaimTaupe().equals("aucun");
		}
		return false;
	}

}
