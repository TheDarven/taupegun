package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.statsgame.RestGame;
import fr.thedarven.statsgame.RestPlayerDeath;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;
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
		super(main, new String[]{ "taupegun.revive" });
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		PlayerTaupe targetedPl = PlayerTaupe.getPlayerTaupeByName(args[0]);
		if (Objects.isNull(targetedPl) || targetedPl.isAlive())
			return;

		Player targetedPlayer = targetedPl.getPlayer();
		if (Objects.isNull(targetedPlayer))
			return;

		respawnPlayer(sender, targetedPl, targetedPlayer);
	}

	public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		if (args.length > 0 && !UtilsClass.molesEnabled()) {
			return super.validateCommand(sender, pl, cmd, alias, args);
		} else {
			sender.sendMessage("§a[TaupeGun]§c " + LanguageBuilder.getContent("COMMAND", "cannotRevive", true));
		}
		return false;
	}

	private void respawnPlayer(Player sender, PlayerTaupe targetedPl, Player targetedPlayer) {
		TeamCustom team = targetedPl.getStartTeam();
		if (Objects.isNull(team) || team.isSpectator())
			return;

		Location respawnLocation = null;
		for (PlayerTaupe mate: team.getPlayers()) {
			Player matePlayer = mate.getPlayer();
			if (!Objects.isNull(matePlayer)) {
				respawnLocation = matePlayer.getLocation();
				break;
			}
		}

		if (Objects.isNull(respawnLocation)) {
			World world = UtilsClass.getWorld();
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

		for (RestPlayerDeath playerDeath: RestGame.getCurrentGame().getStats().getPlayerDeath()) {
			if (playerDeath.getVictim().equals(targetedPl.getUuid())) {
				playerDeath.setRevived(true);
			}
		}

		announceRespawn(targetedPlayer.getName());
	}

	private void announceRespawn(String revivedName) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH , 1, 1);
		}

		Map<String, String> params = new HashMap<>();
		params.put("playerName", revivedName);
		String reviveMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("COMMAND", "revive", true), params);

		Bukkit.broadcastMessage("§a[TaupeGun]§f " + reviveMessage);
	}

}
