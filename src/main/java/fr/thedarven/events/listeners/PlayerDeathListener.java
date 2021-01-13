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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.thedarven.models.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.statsgame.RestGame;
import fr.thedarven.statsgame.RestPlayerDeath;
import fr.thedarven.statsgame.RestPlayerKill;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

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
		if (!Objects.isNull(killer)){
			PlayerTaupe pcKiller = PlayerTaupe.getPlayerManager(killer.getUniqueId());
			pcKiller.setKill(pcKiller.getKill() + 1);
			this.main.getDatabaseManager().updateMoleKills(killer);
		}
		
		if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
			String lastEntityTypeName = !Objects.isNull(victim.getLastDamageCause()) && !Objects.isNull(victim.getLastDamageCause().getEntityType())
					? victim.getLastDamageCause().getEntityType().toString() 
					: null;
			RestGame.getCurrentGame().addPlayerDeath(new RestPlayerDeath(victim.getUniqueId(),
					victim.getLastDamageCause().getCause().toString(), lastEntityTypeName));
			if (!Objects.isNull(killer) && killer.getGameMode() == GameMode.SURVIVAL){
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

		if (EnumGameState.isCurrentState(EnumGameState.GAME)){
			pl.setAlive(false);
			
			/* ON S'OCCUPE DU JOUEUR */
			TeamCustom team = pl.getTeam();
			for (Player playerOnline : Bukkit.getOnlinePlayers()) {
				playerOnline.playSound(playerOnline.getLocation(), Sound.WITHER_SPAWN, 1, 1);
			}
			
			if (UtilsClass.molesEnabled() && pl.isOnline()){
				Map<String, String> params = new HashMap<>();
				params.put("playerName", pl.getName());
				String headName = "§6" + TextInterpreter.textInterpretation(LanguageBuilder.getContent("ITEM", "head", true), params);
				
				ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				SkullMeta teteM = (SkullMeta) tete.getItemMeta();
				teteM.setOwner(pl.getName());
				teteM.setDisplayName(headName);
				tete.setItemMeta(teteM);

				if (!Objects.isNull(pl.getPlayer())) {
					pl.getPlayer().getWorld().dropItem(pl.getPlayer().getLocation(), tete);
				}
			}
			
			Player p = pl.getPlayer();
			if (!Objects.isNull(p)){
				p.getPlayer().setGameMode(GameMode.SPECTATOR);
				World world = this.main.getWorldManager().getWorld();
				if (!Objects.isNull(world)) {
					p.getPlayer().teleport(new Location(world,0,200,0));
				}
				p.getPlayer().sendMessage("§c" + LanguageBuilder.getContent("EVENT_DEATH", "deathMumble", true));
				p.getPlayer().sendMessage("§c" + LanguageBuilder.getContent("EVENT_DEATH", "deathInfo", true));
			}
			
			this.main.getDatabaseManager().updateMoleDeath(pl.getUuid().toString(), 1);
			if (!Objects.isNull(team)) {
				team.leaveTeam(pl.getUuid());
			}
			TeamCustom.getSpectatorTeam().joinTeam(pl.getUuid());
		}
	}
}
