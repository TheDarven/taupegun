package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.utils.PermissionHelper;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class PlayerkillCommand extends OperatorCommand {

	public PlayerkillCommand(TaupeGun main){
		super(main, new EnumGameState[] { EnumGameState.GAME }, new String[]{PermissionHelper.PLAYER_KILL_COMMAND});
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		PlayerTaupe targetedPl = PlayerTaupe.getPlayerTaupeByName(args[0]);
		if (Objects.isNull(targetedPl) || !targetedPl.isAlive()) {
			return;
		}

		Player targetedPlayer = pl.getPlayer();
		if (Objects.nonNull(targetedPlayer)) {
			targetedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1000, 250));
		} else {
			this.main.getListenerManager().getDeathListener().killPlayer(targetedPl, true);
		}
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (args.length > 0) {
			return super.validateCommand(sender, pl, cmd, alias, args);
		}
		return false;
	}


}
