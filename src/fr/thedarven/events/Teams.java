package fr.thedarven.events;

import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryDeleteTeams;
import fr.thedarven.configuration.builders.InventoryParametres;
import fr.thedarven.configuration.builders.InventoryPlayers;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.configuration.builders.InventoryTeams;
import fr.thedarven.main.EnumGame;
import fr.thedarven.main.PlayerTaupe;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.utils.CodeColor;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.api.ScoreboardSign;

public class Teams implements Listener {
	
public int a= -1;
public static ScoreboardManager manager = Bukkit.getScoreboardManager();
public static Scoreboard board = manager.getNewScoreboard();
static Objective objective = board.registerNewObjective("health", "health");


	public Teams(TaupeGun pl) {
	}
	@EventHandler
	public void join(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(board);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClickInventory(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null){
			return;
		}
		final Player p = (Player) e.getWhoClicked();
		final PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
		
		if(TaupeGun.etat.equals(EnumGame.LOBBY) && e.getCurrentItem() != null){
			if(e.getCurrentItem().equals(InventoryRegister.addteam.getItem())) {
				e.setCancelled(true);
				new AnvilGUI(TaupeGun.getInstance(),p, new AnvilGUI.AnvilClickHandler() {
					
					@Override
				    public boolean onClick(AnvilGUI menu, String text) {
				    	pl.setCreateTeamName(text);
				    	Bukkit.getScheduler().runTask(TaupeGun.getInstance(), new Runnable() {

				    		@Override
				    		public void run() {
					    		/* OUVERTURE DE L'INVENTAIRE COULEUR */
				    			p.openInventory(InventoryRegister.choisirCouleur.getInventory());
					    		if(pl.getCreateTeamName() == null) {
					    			p.closeInventory();
					    			return;
					    		}
					    		if(pl.getCreateTeamName().length() > 16){
					    			p.closeInventory();
					    			MessagesClass.CannotTeamCreateNameBigMessage(p);
					    			pl.setCreateTeamName(null);
					    			return;
					    		}
					    			   
					    		if(pl.getCreateTeamName().equals("Spectateurs") || pl.getCreateTeamName().equals("Taupes1") || pl.getCreateTeamName().equals("Taupes2") || pl.getCreateTeamName().equals("SuperTaupe") || pl.getCreateTeamName().equals("aucune")){
					    			p.closeInventory();
				    				MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
				    				pl.setCreateTeamName(null);
					    			return;
				    			}
					    		
					    		Set<Team> teams = board.getTeams();
					    		for(Team team : teams){
					    			if(pl.getCreateTeamName().equals(team.getName())){
					    				p.closeInventory();
					    				MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
					    				pl.setCreateTeamName(null);
						    			return;
					    			}
					    		}
					    	}
				    	});
				    	return true;
				    }
				}).setInputName("Choix du nom").open();
			}else if(e.getInventory().getTitle().equalsIgnoreCase("Choix de la couleur")){
				
				/* POUR CHOISIR SA COULEUR */
				e.setCancelled(true);
				if(e.getCurrentItem().getType() == Material.BANNER){
					Set<Team> teams = board.getTeams();
		    		for(Team team : teams){
		    			if(pl.getCreateTeamName().equals(team.getName())){
		    				p.closeInventory();
		    				MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
		    				pl.setCreateTeamName(null);
			    			return;
		    			}
		    		}
					byte tempColor = ((BannerMeta)e.getCurrentItem().getItemMeta()).getBaseColor().getData();
					newTeam(pl.getCreateTeamName(), tempColor);
					
					MessagesClass.TeamCreateMessage(p, pl.getCreateTeamName());
					pl.setCreateTeamName(null);
					p.openInventory(InventoryTeams.getLastChild().getInventory());
				}
			}
		}
	}

	public static void newTeam(String name, int color){
		Team owner = board.registerNewTeam(name);
		
		owner.setPrefix("§"+CodeColor.codeColorBP(color));
		owner.setSuffix("§f");
		scoreboardPlayer();
		InventoryTeams inv = new InventoryTeams(name, CodeColor.codeColorPB(CodeColor.codeColorBP(color)));
		new InventoryParametres(inv);
		new InventoryPlayers(inv);
		new InventoryDeleteTeams(inv);
	}
	
	public static void newTeam(String name, String color){
		Team owner = board.registerNewTeam(name);
		
		if(name.equals("Taupes1") || name.equals("Taupes2") || name.equals("SuperTaupe")) {
			owner.setPrefix("§"+color+"["+name+"] ");
		}else {
			owner.setPrefix("§"+color);
		}
		owner.setSuffix("§f");
		scoreboardPlayer();
		InventoryTeams inv = new InventoryTeams(name, CodeColor.codeColorPB(color));
		new InventoryParametres(inv);
		new InventoryPlayers(inv);
		new InventoryDeleteTeams(inv);
	}
	
	public static void deleteTeam(String name){
		Team owner = board.getTeam(name);
		owner.unregister();
		scoreboardPlayer();
		InventoryTeams.removeTeam(name);
	}
	
	public static void joinTeam(String name, String p){
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(p.equals(player.getName())){
				
				objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
				@SuppressWarnings("deprecation")
				Score score = objective.getScore(player);
				score.setScore(20);
				
				player.setScoreboard(board);
				if(!TaupeGun.etat.equals(EnumGame.GAME) || PlayerTaupe.getPlayerManager(player.getUniqueId()).getTeamName().equals("aucune")) {
					PlayerTaupe.getPlayerManager(player.getUniqueId()).setTeamName(name);
				}
			}
		}
		Team owner = board.getTeam(name);
		owner.addEntry(p);
		scoreboardPlayer();
	}
	
	public static void joinInitTeam(String name, String p){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(p.equals(player.getName())){
				
				objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
				@SuppressWarnings("deprecation")
				Score score = objective.getScore(player);
				score.setScore(20);
				
				player.setScoreboard(board);
			}
		}
		Team owner = board.getTeam(name);
		owner.addEntry(p);
		scoreboardPlayer();
	}
	
	@SuppressWarnings("deprecation")
	public static void leaveTeam(String name, String p){
		Team owner = board.getTeam(name);
		owner.removeEntry(p);
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(p.equals(player.getName())){
				board.resetScores(player);
				if(!TaupeGun.etat.equals(EnumGame.GAME)) {
					PlayerTaupe.getPlayerManager(player.getUniqueId()).setTeamName("aucune");
				}
			}
		}
		scoreboardPlayer();
	}
	
	/* public static void renameTeam(String pNomAvant, String pNomApres) {
		Team owner = board.registerNewTeam(pNomApres);
		owner.setPrefix(board.getTeam(pNomAvant).getPrefix());
		owner.setSuffix(board.getTeam(pNomAvant).getSuffix());
		
		for(String p : board.getTeam(pNomAvant).getEntries()) {
			leaveTeam(pNomAvant, p);
			joinTeam(pNomApres, p);
		}
		board.getTeam(pNomAvant).unregister();;
		
		scoreboardPlayer();
	} */
	
	public static void scoreboardPlayer(){
		for(Entry<Player, ScoreboardSign> sign : Login.boards.entrySet()){
			int playerTeam = 0;
			int teamNick = 0;
			Set<Team> teams = board.getTeams();
			for(Team team : teams){
				if(team.getName().equals("Spectateurs") || team.getName().equals("Taupes1") || team.getName().equals("Taupes2") || team.getName().equals("SuperTaupe")){
					teamNick++;
				}
				if(team.getName() != "Spectateurs"){
					playerTeam += team.getEntries().size();	
				}
			}
			sign.getValue().setLine(7, "➊ Joueurs :§e "+playerTeam+" ("+(board.getTeams().size()-teamNick)+")");
			
			// LE MUR EST A PLUS DE 59:59 //
			if(InventoryRegister.murtime.getValue()*60 - TaupeGun.timer >= 6000){
				String dateformatMur = DurationFormatUtils.formatDuration(((int) InventoryRegister.murtime.getValue()*60 - TaupeGun.timer)*1000, "mmm:ss");
				sign.getValue().setLine(11, "➍ Mur :§e "+dateformatMur);
			}
			
			// LE MUR EST A MOINS DE 100:00 //
			else{
				String dateformatMur = DurationFormatUtils.formatDuration(((int) InventoryRegister.murtime.getValue()*60 - TaupeGun.timer)*1000, "mm:ss");
				sign.getValue().setLine(11, "➍ Mur :§e "+dateformatMur);
			}
		}
	}
}