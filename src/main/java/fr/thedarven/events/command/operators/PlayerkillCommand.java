package fr.thedarven.events.command.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.helpers.PermissionHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerkillCommand extends OperatorCommand implements TabCompleter {

	public PlayerkillCommand(TaupeGun main){
		super(main, new EnumGameState[] { EnumGameState.GAME }, new String[]{PermissionHelper.PLAYER_KILL_COMMAND});
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		PlayerTaupe targetedPl = PlayerTaupe.getPlayerTaupeByName(args[0]);
		if (Objects.isNull(targetedPl) || !targetedPl.isAlive()) {
			return;
		}

		Player targetedPlayer = targetedPl.getPlayer();
		if (Objects.nonNull(targetedPlayer)) {
			targetedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1000, 250));
		} else {
			this.main.getListenerManager().getDeathListener().killPlayer(targetedPl);
		}
	}

	public boolean canPlayerExecuteCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (args.length > 0) {
			return super.canPlayerExecuteCommand(sender, pl, cmd, alias, args);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return PlayerTaupe.getAlivePlayerManager().stream()
					.map(PlayerTaupe::getName)
					.collect(Collectors.toList());
		}
		return Bukkit.getOnlinePlayers().stream()
				.map(Player::getName)
				.collect(Collectors.toList());
	}
}
