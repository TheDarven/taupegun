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
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.ScoreboardModule;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.api.Title;
import net.md_5.bungee.api.ChatColor;

public class Login implements Listener {

	public Login(TaupeGun taupeGun) {	
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
		
		SqlRequest.updatePlayerLast(e.getPlayer());
		ScoreboardModule.joinScoreboard(p);
		e.setJoinMessage(ChatColor.DARK_GRAY+"("+ChatColor.GREEN+"+"+ChatColor.DARK_GRAY+") "+ChatColor.GRAY+e.getPlayer().getName());
		
		if(TaupeGun.etat.equals(EnumGame.LOBBY)) {
			InventoryPlayers.reloadInventory();
			if(TaupeGun.developpement && Bukkit.getOnlinePlayers().size() == 1) {
				Title.title(p, ChatColor.GOLD +"Mode test : ON", "", 20);
			}
		}else if(TaupeGun.etat.equals(EnumGame.GAME)) {
			if(!InventoryRegister.coordonneesvisibles.getValue())
				DisableF3.disableF3(p);
			if(!pl.isAlive()) {
				p.setGameMode(GameMode.SPECTATOR);
				p.teleport(new Location(Bukkit.getWorld("world"),0,200,0));
			}
		}
	}
	
	@EventHandler
    public void PlayerQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		e.setQuitMessage(ChatColor.DARK_GRAY+"("+ChatColor.RED+"-"+ChatColor.DARK_GRAY+") "+ChatColor.GRAY+e.getPlayer().getName());
		
		if(ScoreboardModule.boards.containsKey(player)){
			ScoreboardModule.boards.get(player).destroy();
		}
		
		if(TaupeGun.etat.equals(EnumGame.LOBBY)) {
			new BukkitRunnable(){
				@Override
				public void run() {
					InventoryPlayers.reloadInventory();
					this.cancel();
				}
			}.runTaskTimer(TaupeGun.instance,0, 20);
		}else if(TaupeGun.etat.equals(EnumGame.WAIT)){
			Bukkit.getScheduler().cancelAllTasks();
			TaupeGun.timerStart = 10;
			TaupeGun.etat = EnumGame.LOBBY;
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.WITHER_HURT , 1, 1);
				Title.title(p, ChatColor.RED +"Lancement annulé", "", 10);
            }
			
			for(PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
				pl.setTaupeTeam(-1);
				pl.setSuperTaupeTeam(-1);
			}
		}
		
		SqlRequest.updatePlayerTimePlay(player);
		SqlRequest.updatePlayerLast(player);
	}
}
