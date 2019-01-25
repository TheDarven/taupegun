package fr.thedarven.events.commands;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.EnumGame;
import fr.thedarven.main.Game;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.api.Title;


public class StartCommand implements Listener {
Scoreboard board = Teams.board;

	public StartCommand(TaupeGun pl) {
	}
	
	@EventHandler
	public void onCommandes(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String msg = e.getMessage();
		String[] args = msg.split(" ");
			
		/* SI ON FAIT LA BONNE COMMANDE */
		if(args[0].equalsIgnoreCase("/start")){
			e.setCancelled(true);
			
			/* if(!p.isOp()){
				MessagesClass.CannotCommandOperatorMessage(p);
				return;
			}
			if(!TaupeGun.etat.equals(EnumGame.LOBBY)){
				MessagesClass.CannotCommandStartAlreadyStartMessage(p);
				return;
			}
			
			Set<Team> teams = board.getTeams();
			if(teams.size() < 2 && !InventoryRegister.supertaupes.getValue()){
				MessagesClass.CannotCommandStartTeam1Message(p);
				return;
			}
			
			if(teams.size() < 3 && InventoryRegister.supertaupes.getValue()){
				MessagesClass.CannotCommandStartTeam2Message(p);
				return;
			} 
			
			for(Team team : teams){
				if(InventoryRegister.nombretaupes.getValue() == 1 && team.getEntries().size() < 3 || InventoryRegister.nombretaupes.getValue() == 2 && team.getEntries().size() < 4){
					MessagesClass.CannotCommandStartPlayerMessage(p);
					return;
				}
				for(String player : team.getEntries()){
					boolean online = false;
					for(Player login : Bukkit.getServer().getOnlinePlayers()){
						if(player.equals(login.getName())){
							online = true;
						}
					}
					if(online == false){
						MessagesClass.CannotCommandStartOfflinePlayerMessage(p);
						return;
					}
				}
			} */
			MessagesClass.CommandStartStartMessage(p);
			TaupeGun.etat = EnumGame.WAIT;
			Bukkit.getScheduler().runTaskTimer(TaupeGun.instance, new Runnable(){
				@Override
				public void run(){
					if(TaupeGun.timerStart == 10){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.DARK_RED +"10", "", 10);
						}
					}
												
					if(TaupeGun.timerStart == 5){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.RED +"5", "", 10);
						}
					}
					if(TaupeGun.timerStart == 4){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.YELLOW +"4", "", 10);
						}
					}
					if(TaupeGun.timerStart == 3){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.GOLD +"3", "", 10);
						}
					}
					if(TaupeGun.timerStart == 2){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.GREEN +"2", "", 10);
						}
					}
					if(TaupeGun.timerStart == 1){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.DARK_GREEN +"1", "", 10);
						}
					}						
					if(TaupeGun.timerStart == 0){
						Bukkit.getScheduler().cancelAllTasks();
						Game.start();
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL , 1, 1);
							Title.title(player, ChatColor.DARK_GREEN +"Go !", "", 10);
						}
					}
					TaupeGun.timerStart--;
				}
			},20,20);		
		}	
	}
}