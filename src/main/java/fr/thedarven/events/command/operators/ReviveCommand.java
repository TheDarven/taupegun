package fr.thedarven.events.command.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.ReviveRecap;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.stats.model.dto.GameDto;
import fr.thedarven.stats.model.dto.PlayerDeathDto;
import fr.thedarven.utils.helpers.PermissionHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReviveCommand extends OperatorCommand {

	public ReviveCommand(TaupeGun main){
		super(main, new EnumGameState[] { EnumGameState.GAME }, new String[]{PermissionHelper.REVIVE_COMMAND });
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		PlayerTaupe targetedPl = PlayerTaupe.getPlayerTaupeByName(args[0]);
		if (Objects.isNull(targetedPl) || targetedPl.isAlive()) {
			return;
		}

		Player targetedPlayer = targetedPl.getPlayer();
		if (Objects.isNull(targetedPlayer)) {
			return;
		}

		respawnPlayer(sender, targetedPl, targetedPlayer);
	}

	public boolean canPlayerExecuteCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (args.length > 0 && !this.main.getGameManager().areMolesRevealed() && TeamCustom.getAllAliveTeams().size() > 1) {
			return super.canPlayerExecuteCommand(sender, pl, cmd, alias, args);
		} else {
			sender.sendMessage("§a[TaupeGun]§c " + LanguageBuilder.getContent("COMMAND", "cannotRevive", true));
		}
		return false;
	}

	private void respawnPlayer(Player sender, PlayerTaupe targetedPl, Player targetedPlayer) {
		TeamCustom team = targetedPl.getStartTeam();
		if (Objects.isNull(team) || team.isSpectator())
			return;

		if (!team.isAlive()) {
			this.main.getDatabaseManager().updateTeamDeath(team.getTeam().getName(), false);
			team.setAlive(true);
		}

		Location respawnLocation = null;
		for (PlayerTaupe mate: team.getAlivesPlayers()) {
			Player matePlayer = mate.getPlayer();
			if (Objects.nonNull(matePlayer) && matePlayer != sender) {
				respawnLocation = matePlayer.getLocation();
				break;
			}
		}

		if (Objects.isNull(respawnLocation)) {
			World world = this.main.getWorldManager().getWorld();
			if (Objects.isNull(world)) {
				sender.sendMessage("§a[TaupeGun]§c " + LanguageBuilder.getContent("COMMAND", "cannotReviveWorldNotFound", true));
				return;
			}
			respawnLocation = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 2, 0);
		}

		team.joinTeam(targetedPl);
		targetedPlayer.teleport(respawnLocation);
		targetedPlayer.setGameMode(GameMode.SURVIVAL);
		targetedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 2));
		targetedPl.setAlive(true);
		main.getDatabaseManager().updateMoleDeath(targetedPl.getUuid().toString(),0);

		for (PlayerDeathDto playerDeath: GameDto.getCurrentGame().getStats().getPlayerDeath()) {
			if (playerDeath.getVictim().equals(targetedPl.getUuid())) {
				playerDeath.setRevived(true);
			}
		}

		this.main.getGameManager().addToRecap(new ReviveRecap(targetedPl));

		announceRespawn(targetedPlayer.getName());
	}

	private void announceRespawn(String revivedName) {
		this.main.getPlayerManager().sendPlaySound(Sound.ENDERDRAGON_DEATH);

		Map<String, String> params = new HashMap<>();
		params.put("playerName", revivedName);
		String reviveMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("COMMAND", "revive", true), params);

		Bukkit.broadcastMessage("§a[TaupeGun]§f " + reviveMessage);
	}

}
