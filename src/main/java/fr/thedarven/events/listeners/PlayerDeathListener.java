package fr.thedarven.events.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.thedarven.TaupeGun;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.statsgame.RestGame;
import fr.thedarven.statsgame.RestPlayerDeath;
import fr.thedarven.statsgame.RestPlayerKill;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {

	private TaupeGun main;

	public PlayerDeathListener(TaupeGun main) {
		this.main = main;
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		Player killer = victim.getKiller();
		
		Map<String, String> params = new HashMap<>();
		params.put("playerName", "§6" + victim.getName() + "§r");
		String deathAllMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("EVENT_DEATH", "deathAll", true), params);
		
		e.setDeathMessage(deathAllMessage);
		killPlayer(PlayerTaupe.getPlayerManager(e.getEntity().getUniqueId()),false);
		if (Objects.nonNull(killer)){
			PlayerTaupe pcKiller = PlayerTaupe.getPlayerManager(killer.getUniqueId());
			pcKiller.setKill(pcKiller.getKill() + 1);
			this.main.getDatabaseManager().updateMoleKills(killer);
		} else {
			this.main.getGameManager().incrementPveKills();
		}
		
		if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
			String lastEntityTypeName = Objects.nonNull(victim.getLastDamageCause()) && Objects.nonNull(victim.getLastDamageCause().getEntityType())
					? victim.getLastDamageCause().getEntityType().toString() 
					: null;
			RestGame.getCurrentGame().addPlayerDeath(new RestPlayerDeath(victim.getUniqueId(),
					victim.getLastDamageCause().getCause().toString(), lastEntityTypeName));
			if (Objects.nonNull(killer) && killer.getGameMode() == GameMode.SURVIVAL){
				RestGame.getCurrentGame().addPlayerKill(new RestPlayerKill(victim.getUniqueId(), killer.getUniqueId(), killer.getHealth()));
			}
		}
	}
	
	public void killPlayer(PlayerTaupe pl, boolean showMessage) {
		if (showMessage) {
			Map<String, String> params = new HashMap<>();
			params.put("playerName", "§6" + pl.getName() + "§r");
			String deathAllMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("EVENT_DEATH", "deathAll", true), params);
			Bukkit.broadcastMessage(deathAllMessage);
		}

		if (!EnumGameState.isCurrentState(EnumGameState.GAME)) {
			return;
		}

		pl.setAlive(false);

		/* ON S'OCCUPE DU JOUEUR */
		Player deadPlayer = pl.getPlayer();
		TeamCustom team = pl.getTeam();
		this.main.getPlayerManager().sendPlaySound(Sound.WITHER_SPAWN);

		if (Objects.nonNull(deadPlayer)){
			if (this.main.getGameManager().molesEnabled() && pl.isOnline()) {
				if (this.main.getScenariosManager().goldenHead.getValue() > 0) {
					dropHeadItem(pl, deadPlayer);
				}

				int nbGoldenApple = this.main.getScenariosManager().deathGoldenApple.getIntValue();
				if (nbGoldenApple > 0) {
					deadPlayer.getWorld().dropItem(deadPlayer.getLocation(), new ItemStack(Material.GOLDEN_APPLE, nbGoldenApple));
				}
			}

			deadPlayer.setGameMode(GameMode.SPECTATOR);
			World world = this.main.getWorldManager().getWorld();
			if (Objects.nonNull(world)) {
				deadPlayer.teleport(new Location(world,0,200,0));
			}

			if (this.main.getScenariosManager().kickOnDeath.getValue()) {
				deadPlayer.kickPlayer( LanguageBuilder.getContent("EVENT_DEATH", "deathMumble", true));
			} else {
				deadPlayer.sendMessage("§c" + LanguageBuilder.getContent("EVENT_DEATH", "deathMumble", true));
				deadPlayer.sendMessage("§c" + LanguageBuilder.getContent("EVENT_DEATH", "deathInfo", true));
			}
		}

		this.main.getDatabaseManager().updateMoleDeath(pl.getUuid().toString(), 1);
		if (Objects.nonNull(team)) {
			team.leaveTeam(pl.getUuid());
		}
		TeamCustom.getSpectatorTeam().joinTeam(pl);
	}

	private void dropHeadItem(PlayerTaupe pl, Player deadPlayer) {
		Map<String, String> params = new HashMap<>();
		params.put("playerName", pl.getName());
		String headName = "§6" + TextInterpreter.textInterpretation(LanguageBuilder.getContent("ITEM", "head", true), params);
		deadPlayer.getWorld().dropItem(deadPlayer.getLocation(), this.main.getPlayerManager().getHeadOfPlayer(pl.getName(), headName));
	}
}
