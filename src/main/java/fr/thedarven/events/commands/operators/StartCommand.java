package fr.thedarven.events.commands.operators;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import fr.thedarven.utils.UtilsClass;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.teams.TeamGraph;

public class StartCommand implements CommandExecutor{

	private TaupeGun main;

	public StartCommand(TaupeGun main){
		this.main = main;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("start")) {
				if(p.isOp() || p.hasPermission("taupegun.start")){
					if(EnumGameState.isCurrentState(EnumGameState.LOBBY)){

						if(UtilsClass.getWorld() == null) {
							String worldNotExistMessage = "§e"+LanguageBuilder.getContent("START_COMMAND", "worldNotExist", true);
							Bukkit.broadcastMessage(worldNotExistMessage);
							return true;
						}

						Scoreboard board = TeamCustom.board;
						Set<Team> teams = board.getTeams();
						
						if(teams.size() < 2 && !this.main.getInventoryRegister().supertaupes.getValue()){
							p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "needTwoTeams", true));
							return true;
						}
						
						if(teams.size() < 3 && this.main.getInventoryRegister().supertaupes.getValue()){
							p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "needThreeTeams", true));
							return true;
						}
						
						if(this.main.getInventoryRegister().kits.getChilds().size() <= 1) {
							p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "notEnoughKits", true));
							return true;
						}
						
						for(Team team : teams){
							if((this.main.getInventoryRegister().nombretaupes.getValue() == 1 && team.getEntries().size() == 2) || this.main.getInventoryRegister().nombretaupes.getValue() == 2 && team.getEntries().size() < 4){
								p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "notEnoughPlayersPerTeam", true));
								return true;
							}
							boolean online = false;
							for(String player : team.getEntries()){
								online = false;
								for(Player login : Bukkit.getServer().getOnlinePlayers()){
									if(player.equals(login.getName()))
										online = true;
								}
								if(!online){
									p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "disconnectedPlayer", true));
									return true;
								}
							}
						}
						
						TeamGraph graph = new TeamGraph(this.main);
						
						Random r = new Random();
						for(Team team : teams){
							ArrayList<PlayerTaupe> listTaupes = new ArrayList<PlayerTaupe>();
							if(team.getSize() == 1 || team.getSize() == 3 || (team.getSize() > 3 && this.main.getInventoryRegister().nombretaupes.getValue() == 1)) {
								ArrayList<PlayerTaupe> playerList = new ArrayList<>();
								for(OfflinePlayer player : team.getPlayers()){
									playerList.add(PlayerTaupe.getPlayerManager(player.getUniqueId()));
								}
								listTaupes.add(playerList.get(r.nextInt(team.getSize())));
								graph.addEquipes(listTaupes);
								
							}else if(team.getSize() > 3 && this.main.getInventoryRegister().nombretaupes.getValue() == 2) {
								ArrayList<PlayerTaupe> playerList = new ArrayList<>();
								for(OfflinePlayer player : team.getPlayers()){
									playerList.add(PlayerTaupe.getPlayerManager(player.getUniqueId()));
								}
								int taupeInt1 = r.nextInt(team.getSize());
								int taupeInt2 = r.nextInt(team.getSize());
								while(taupeInt1 == taupeInt2)
									taupeInt2 = r.nextInt(team.getSize());
								listTaupes.add(playerList.get(taupeInt1));
								listTaupes.add(playerList.get(taupeInt2));
								graph.addEquipes(listTaupes);
							}
						}
						boolean resultat = graph.creationEquipes();
						if(!resultat) {
							p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "incorrectMoleNumber", true));
							TeamCustom.deleteTeamTaupe();
							for(PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
								pl.setTaupeTeam(null);
								pl.setSuperTaupeTeam(null);
							}
							return true;
						}
						
						p.sendMessage(ChatColor.BLUE+LanguageBuilder.getContent("START_COMMAND", "gameCanStart", true));
						EnumGameState.setState(EnumGameState.WAIT);
						Bukkit.getScheduler().runTaskTimer(TaupeGun.getInstance(), new Runnable(){

							@Override
							public void run(){
								String title = "";
								Sound sound = Sound.ORB_PICKUP;

								switch(main.getGameManager().getCooldownTimer()){
									case 10:
										title = ChatColor.DARK_RED + "10";
										break;
									case 5:
										title = ChatColor.RED + "5";
										break;
									case 4:
										title = ChatColor.YELLOW + "4";
										break;
									case 3:
										title = ChatColor.GOLD + "3";
										break;
									case 2:
										title = ChatColor.GREEN + "2";
										break;
									case 1:
										title = ChatColor.DARK_GREEN + "1";
										break;
									case 0:
										Bukkit.getScheduler().cancelAllTasks();

										main.getGameManager().startGame();

										sound = Sound.ENDERDRAGON_GROWL;

										title = ChatColor.DARK_GREEN + LanguageBuilder.getContent("START_COMMAND", "gameIsStarting", true);
										break;
								}

								for (Player player : Bukkit.getOnlinePlayers()) {
									player.playSound(player.getLocation(), sound, 1, 1);
									Title.title(player, title, "", 10);
								}

								main.getGameManager().decreaseCooldownTimer();
							}
						},20,20);
					}else {
						p.sendMessage(ChatColor.RED+LanguageBuilder.getContent("START_COMMAND", "gameAlreadyStarted", true));
					}
				}else {
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}
		}
		return true;
	}

}
