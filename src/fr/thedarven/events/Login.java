package fr.thedarven.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryPlayers;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.api.ScoreboardSign;
import fr.thedarven.utils.api.Title;
import net.md_5.bungee.api.ChatColor;

public class Login implements Listener {
	
public static ScoreboardManager manager = Bukkit.getScoreboardManager();
public static Scoreboard board = manager.getNewScoreboard();
public static Map<Player, ScoreboardSign> boards = new HashMap<>();

	public Login(TaupeGun taupeGun) {	
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerTaupe.getPlayerManager(p.getUniqueId());
		
		SqlRequest.updatePlayerLast(e.getPlayer());
		joinScoreboard(p);
		e.setJoinMessage(ChatColor.DARK_GRAY+"("+ChatColor.GREEN+"+"+ChatColor.DARK_GRAY+") "+ChatColor.GRAY+e.getPlayer().getName());
		
		if(TaupeGun.etat.equals(EnumGame.LOBBY)) {
			InventoryPlayers.reloadInventory();
			if(TaupeGun.developpement && Bukkit.getOnlinePlayers().size() == 1) {
				Title.title(p, ChatColor.GOLD +"Mode test : ON", "", 20);
			}
		}
	}
	
	@EventHandler
    public void PlayerQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		e.setQuitMessage(ChatColor.DARK_GRAY+"("+ChatColor.RED+"-"+ChatColor.DARK_GRAY+") "+ChatColor.GRAY+e.getPlayer().getName());
		
		if(Login.boards.containsKey(player)){
			Login.boards.get(player).destroy();
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
		}
		
		SqlRequest.updatePlayerTimePlay(player);
		SqlRequest.updatePlayerLast(player);
	}
	
	public static void joinScoreboard(Player p) {			
		ScoreboardSign scoreboard = new ScoreboardSign(p, "§6=== TaupeGun ===");
		scoreboard.create();		
		scoreboard.setLine(6, "§a ");
		
		int playerTeam = 0;
		int teamNick = 0;
		Set<Team> teams = Teams.board.getTeams();
		for(Team team : teams){
			if(team.getName().equals("Spectateurs") || team.getName().equals("Taupes1") || team.getName().equals("Taupes2") || team.getName().equals("SuperTaupe")){
				teamNick++;
			}
			if(team.getName() != "Spectateurs"){
				playerTeam += team.getEntries().size();	
			}
		}
		
		scoreboard.setLine(7, "➊ Joueurs :§e "+playerTeam+" ("+(Teams.board.getTeams().size()-teamNick)+")");
		scoreboard.setLine(8, "➋ Centre :§e 0");
		scoreboard.setLine(9, "➌ Kills :§e "+PlayerTaupe.getPlayerManager(p.getUniqueId()).getKill());
		scoreboard.setLine(10, "§b ");
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer >= 6000){
			String dateformatMur = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mmm:ss");
			scoreboard.setLine(11, "➍ Mur :§e "+dateformatMur);	
		}else{
			String dateformatMur = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mm:ss");
			scoreboard.setLine(11, "➍ Mur :§e "+dateformatMur);	
		}
		
		String dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer*1000, "mm:ss");
		scoreboard.setLine(12, "➎ Chrono :§e "+dateformatChrono);
		
		scoreboard.setLine(13, "§c ");
		scoreboard.setLine(14, "➏ Bordures :§e "+Bukkit.getServer().getWorld("world").getWorldBorder().getSize()/2);
		boards.put(p, scoreboard);
	}
}
