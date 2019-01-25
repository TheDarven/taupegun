package fr.thedarven.utils;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scoreboard.Team;

import fr.thedarven.events.Teams;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.api.Title;
import net.md_5.bungee.api.ChatColor;

public class MessagesEventClass {
	
	
	//TAUPECOMMAND	
	public static void CommandTaupe1MessageMessage(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		String messageCommand = ChatColor.RED+e.getPlayer().getName()+":";
		for(int messageSize = 1; messageSize < args.length; messageSize++){
			messageCommand = messageCommand+" "+args[messageSize];
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			if(PlayerTaupe.getPlayerManager(player.getUniqueId()).getTaupeTeam() == 1){
				player.sendMessage(messageCommand);
			}
		}
	}
	
	public static void CommandTaupe2MessageMessage(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		String messageCommand = ChatColor.RED+e.getPlayer().getName()+":";
		for(int messageSize = 1; messageSize < args.length; messageSize++){
			messageCommand = messageCommand+" "+args[messageSize];
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			if(PlayerTaupe.getPlayerManager(player.getUniqueId()).getTaupeTeam() == 2){
				player.sendMessage(messageCommand);
			}
		}
	}
	
	public static void CommandSupertaupeMessageMessage(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		String messageCommand = ChatColor.DARK_RED+e.getPlayer().getName()+":";
		for(int messageSize = 1; messageSize < args.length; messageSize++){
			messageCommand = messageCommand+" "+args[messageSize];
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			if(PlayerTaupe.getPlayerManager(player.getUniqueId()).getSuperTaupeTeam() == 1){
				player.sendMessage(messageCommand);
			}
		}
	}
	
	public static void CommandTaupe1MessageSpectatorMessage(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		String messageCommand = ChatColor.RED+e.getPlayer().getName()+":";
		for(int messageSize = 1; messageSize < args.length; messageSize++){
			messageCommand = messageCommand+" "+args[messageSize];
		}
		Set<Team> teams = Teams.board.getTeams();
		for(Team team : teams){
			if(team.getName().equals("Spectateurs")){
				for(String player : team.getEntries()){
					Bukkit.getPlayer(player).sendMessage(ChatColor.RED+"[TAUPE 1] "+messageCommand);
				}
			}
		}	
	}
	
	public static void CommandTaupe2MessageSpectatorMessage(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		String messageCommand = ChatColor.RED+e.getPlayer().getName()+":";
		for(int messageSize = 1; messageSize < args.length; messageSize++){
			messageCommand = messageCommand+" "+args[messageSize];
		}
		Set<Team> teams = Teams.board.getTeams();
		for(Team team : teams){
			if(team.getName().equals("Spectateurs")){
				for(String player : team.getEntries()){
					Bukkit.getPlayer(player).sendMessage(ChatColor.RED+"[TAUPE 2] "+messageCommand);
				}
			}
		}	
	}
	
	public static void CommandSupertaupeMessageSpectatorMessage(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		String messageCommand = ChatColor.DARK_RED+e.getPlayer().getName()+":";
		for(int messageSize = 1; messageSize < args.length; messageSize++){
			messageCommand = messageCommand+" "+args[messageSize];
		}
		Set<Team> teams = Teams.board.getTeams();
		for(Team team : teams){
			if(team.getName().equals("Spectateurs")){
				for(String player : team.getEntries()){
					Bukkit.getPlayer(player).sendMessage(ChatColor.DARK_RED+"[SUPERTAUPE] "+messageCommand);
				}
			}
		}	
	}
	
	public static void CommandReavelMessage(PlayerCommandPreprocessEvent e) {
		Bukkit.broadcastMessage(ChatColor.RED+e.getPlayer().getName()+" se révèle être une taupe !");
	}
	
	public static void CommandSuperreavelMessage(PlayerCommandPreprocessEvent e) {
		Bukkit.broadcastMessage(ChatColor.DARK_RED+e.getPlayer().getName()+" se révèle être une supertaupe !");
	}
	
	//TEAMS
	public static void TeamDeletePlayerMessage(InventoryClickEvent e) {
		Title.sendActionBar((Player) e.getWhoClicked(), ChatColor.WHITE+" Le joueur "+ChatColor.YELLOW+ChatColor.BOLD+e.getCurrentItem().getItemMeta().getDisplayName()+ChatColor.RESET+ChatColor.WHITE+" a été supprimé de l'équipe.");
	}
	
	public static void TeamAddPlayerMessage(InventoryClickEvent e) {
		Title.sendActionBar((Player) e.getWhoClicked(), ChatColor.GREEN+" Le joueur "+ChatColor.YELLOW+ChatColor.BOLD+e.getCurrentItem().getItemMeta().getDisplayName()+ChatColor.RESET+ChatColor.GREEN+" a été ajouté à l'équipe.");
	}
}
