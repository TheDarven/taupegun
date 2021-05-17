package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.kits.Kit;
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
		Kit moleKit = pl.getMoleKit();
		moleKit.getConfigurationInventory().giveItems(sender);
		pl.setMoleKit(null);
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (super.validateCommand(sender, pl, cmd, alias, args)) {
			return Objects.nonNull(pl.getMoleKit()) && Objects.nonNull(pl.getMoleKit().getConfigurationInventory());
		}
		return false;
	}

}
