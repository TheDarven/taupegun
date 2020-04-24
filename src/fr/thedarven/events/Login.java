package fr.thedarven.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.configuration.builders.teams.InventoryPlayers;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import net.md_5.bungee.api.ChatColor;

public class Login implements Listener {

	public Login(TaupeGun taupeGun) {	
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
		e.setJoinMessage(ChatColor.DARK_GRAY+"("+ChatColor.GREEN+"+"+ChatColor.DARK_GRAY+") "+ChatColor.GRAY+e.getPlayer().getName());
		
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			InventoryPlayers.reloadInventory();
			if(TaupeGun.developpement) {
				Title.title(p, 
					ChatColor.GOLD+LanguageBuilder.getContent("EVENT_LOGIN", "developerModeTitle", InventoryRegister.language.getSelectedLanguage(), true),
					LanguageBuilder.getContent("EVENT_LOGIN", "developerModeSubtitle", InventoryRegister.language.getSelectedLanguage(), true), 
					40
				);
			}
		}else if(EnumGameState.isCurrentState(EnumGameState.GAME)) {
			if(!InventoryRegister.coordonneesvisibles.getValue())
				DisableF3.disableF3(p);
			if(!pl.isAlive() || pl.getTeam() == null) {
				p.setGameMode(GameMode.SPECTATOR);
				p.teleport(new Location(Bukkit.getWorld("world"),0,200,0));
				TeamCustom.getSpectatorTeam().joinTeam(pl.getUuid());
			}
		}
		loginAction(p);
	}
	
	@EventHandler
    public void PlayerQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		e.setQuitMessage(ChatColor.DARK_GRAY+"("+ChatColor.RED+"-"+ChatColor.DARK_GRAY+") "+ChatColor.GRAY+e.getPlayer().getName());
		
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			new BukkitRunnable(){
				@Override
				public void run() {
					InventoryPlayers.reloadInventory();
					this.cancel();
				}
			}.runTaskTimer(TaupeGun.instance,0, 20);
		}else if(EnumGameState.isCurrentState(EnumGameState.WAIT)){
			Bukkit.getScheduler().cancelAllTasks();
			TaupeGun.timerStart = 10;
			EnumGameState.setState(EnumGameState.LOBBY);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.WITHER_HURT , 1, 1);
				Title.title(p, ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "gameStartingCancelled", InventoryRegister.language.getSelectedLanguage(), true), "", 10);
            }
			
			for(PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
				pl.setTaupeTeam(null);
				pl.setSuperTaupeTeam(null);
			}
			
			TeamCustom.deleteTeamTaupe();
		}
		leaveAction(player);
	}
	
	public static void loginAction(Player p) {
		p.setScoreboard(TeamCustom.board);
		PlayerTaupe.getPlayerManager(p.getUniqueId());
		
		MessagesClass.TabMessage(p);
		TaupeGun.scoreboardManager.onLogin(p);
		SqlRequest.updatePlayerLast(p);
		
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY))
			ScenariosItemInteract.actionBeacon(p);
	}
	
	public static void leaveAction(Player p) {
		TaupeGun.scoreboardManager.onLogout(p);
		SqlRequest.updatePlayerTimePlay(p);
		SqlRequest.updatePlayerLast(p);
		
		ScenariosItemInteract.removeBeacon(p);
	}
}
