package fr.thedarven.events.command.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.kit.model.Kit;
import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ClaimCommand extends MoleCommand {

	public ClaimCommand(TaupeGun main) {
		super(main);
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		Kit moleKit = pl.getMoleKit();
		this.main.getScenariosManager().kitsMenu.getInventoryOfKit(moleKit).ifPresent(kitInventory -> {
			kitInventory.giveItems(sender);
			pl.setMoleKit(null);
		});
	}

	public boolean canPlayerExecuteCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.canPlayerExecuteCommand(sender, pl, cmd, alias, args)) {
			return Objects.nonNull(pl.getMoleKit());
		}
		return false;
	}

}
