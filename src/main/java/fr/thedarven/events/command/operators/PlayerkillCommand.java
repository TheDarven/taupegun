package fr.thedarven.events.command.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.GamePlayerDeathEvent;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.game.model.enums.GameDeathCause;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.helpers.PermissionHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
			targetedPl.setPlayerKillCommand(true);
			targetedPlayer.getPlayer().setHealth(0);
		} else {
			GamePlayerDeathEvent gamePlayerDeathEvent = new GamePlayerDeathEvent(targetedPl, GameDeathCause.PLAYER_KILL_COMMAND);
			Bukkit.getPluginManager().callEvent(gamePlayerDeathEvent);
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
