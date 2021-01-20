package fr.thedarven.events.listeners;

import fr.thedarven.events.runnable.CloseInventoryRunnable;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.thedarven.scenarios.teams.InventoryTeamsPlayers;
import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import net.md_5.bungee.api.ChatColor;

import java.util.Objects;

public class PlayerJoinQuitListener implements Listener {

	private TaupeGun main;
	
	public PlayerJoinQuitListener(TaupeGun main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
		
		e.setJoinMessage("§8(§a+§8) §7" + e.getPlayer().getName());
		
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			InventoryTeamsPlayers.reloadInventories();
			if (this.main.development) {
				Title.title(player,
					ChatColor.GOLD+LanguageBuilder.getContent("EVENT_LOGIN", "developerModeTitle", true),
					LanguageBuilder.getContent("EVENT_LOGIN", "developerModeSubtitle", true),
					40
				);
			}
		} else if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
			if (!this.main.getScenariosManager().coordonneesVisibles.getValue()) {
				DisableF3.disableF3(player);
			}

			if (!pl.isAlive() || Objects.nonNull(pl.getTeam())) {
				player.setGameMode(GameMode.SPECTATOR);
				World world = this.main.getWorldManager().getWorld();
				if (Objects.nonNull(world)) {
					player.teleport(new Location(world,0,200,0));
				}
				TeamCustom.getSpectatorTeam().joinTeam(pl.getUuid());
				
				this.main.getPlayerManager().clearPlayer(player);
			}
		}
		loginAction(player);
	}
	
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		e.setQuitMessage("§8(§c-§8) §7" + e.getPlayer().getName());
		
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			new CloseInventoryRunnable().runTaskTimer(this.main,0, 20);
		} else if (EnumGameState.isCurrentState(EnumGameState.WAIT)) {
			this.main.getCommandManager().getStartCommand().stopStartRunnable();
			this.main.getGameManager().setCooldownTimer(10);

			EnumGameState.setState(EnumGameState.LOBBY);



			this.main.getPlayerManager().sendPlaySoundAndTitle(
					Sound.WITHER_HURT,
					new Title("§c" + LanguageBuilder.getContent("START_COMMAND", "gameStartingCancelled", true), "", 10)
			);



			for (PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
				pl.setTaupeTeam(null);
				pl.setSuperTaupeTeam(null);
			}
			
			TeamCustom.deleteTeamTaupe();
		}
		leaveAction(player);
	}
	
	public void loginAction(Player player) {
		player.setScoreboard(TeamCustom.board);
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
		
		MessagesClass.TabMessage(player);
		this.main.getScoreboardManager().onLogin(player);
		this.main.getDatabaseManager().updatePlayerLast(player);
		
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			this.main.getScenariosManager().scenariosVisible.reloadScenariosItem(player);
		}

		pl.setCustomName(player.getName());
		pl.setLastConnection();
	}
	
	public void leaveAction(Player player) {
		this.main.getScoreboardManager().onLogout(player);
		this.main.getDatabaseManager().updatePlayerTimePlayed(player);
		this.main.getDatabaseManager().updatePlayerLast(player);
		this.main.getScenariosManager().scenariosVisible.removeScenariosItem(player);
		
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
		pl.addTimePlayed((int) (this.main.getDatabaseManager().getLongTimestamp() - pl.getLastConnection()));
	}
}
