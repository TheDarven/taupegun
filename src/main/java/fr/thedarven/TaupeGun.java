package fr.thedarven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import fr.thedarven.database.DatabaseManager;
import fr.thedarven.events.commands.CommandManager;
import fr.thedarven.game.GameManager;
import fr.thedarven.utils.CraftManager;
import fr.thedarven.utils.teams.TeamDeletionManager;
import fr.thedarven.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.LoadThings;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.api.SqlConnection;
import fr.thedarven.utils.api.scoreboard.ScoreboardManager;
import fr.thedarven.events.EventsManager;
import fr.thedarven.statsgame.RestGame;

public class TaupeGun extends JavaPlugin implements Listener{	

	public static TaupeGun instance;

	public boolean development = false;

	/* public static int timerStart = 10;
	public static int timer = 0; */
	
	public SqlConnection sql;
	
	public static InventoryRegister configuration;
	
	public static ScheduledExecutorService executorMonoThread = Executors.newScheduledThreadPool(1);
	public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(16);
	public static ScoreboardManager scoreboardManager = new ScoreboardManager();

	private EventsManager eventsManager;

	private CommandManager commandManager;

	private WorldManager worldManager;

	private DatabaseManager databaseManager;

	private CraftManager craftManager;

	private GameManager gameManager;

	private TeamDeletionManager teamDeletionManager;
	
	public static TaupeGun getInstance(){
		return instance;
	}
	
	@Override
	public void onEnable(){
		instance = this;

		eventsManager = new EventsManager(this);

		commandManager = new CommandManager(this);

		this.saveDefaultConfig();
		
		LoadThings.loadAll(this);

		worldManager = new WorldManager(this);
		worldManager.buildLobby();

		databaseManager = new DatabaseManager(this);

		craftManager = new CraftManager(this);
		
		for(Player p: Bukkit.getOnlinePlayers()){
			this.eventsManager.getLogin().loginAction(p);

			for(PotionEffect potion : p.getActivePotionEffects())
				p.removePotionEffect(potion.getType());

			p.setHealth(20);
			p.setMaxHealth(20.0);
			p.setFoodLevel(20);
			p.setExhaustion(5F);
			p.setExp(0L+0F);
			p.setLevel(0);
		}
		new RestGame();

		gameManager = new GameManager(this);

		teamDeletionManager = new TeamDeletionManager(this);
	}
	
	@Override
	public void onDisable(){
		for(Player p: Bukkit.getOnlinePlayers()){
			if(!InventoryRegister.coordonneesvisibles.getValue())
				DisableF3.enableF3(p);

			this.eventsManager.getLogin().leaveAction(p);
			
			UtilsClass.clearPlayer(p);
		}
		if(this.databaseManager.getGameId() != 0)
			databaseManager.updateGameDuration();
	}
	
	public static ArrayList<String> toLoreItem(String pDescription, String pColor, int pSize){
		ArrayList<String> list = new ArrayList<String>();
		if(pDescription == null)
			return list;
		
		pSize = pSize < 25 ? 25 : pSize;
		List<String> items = Arrays.asList(pDescription.split(" "));
		String ligne = null;
		for(String element: items) {
			if(ligne == null)
				ligne = pColor;
			if(ligne.length() > pColor.length() && (ligne+" "+element).length() > pSize) {
				list.add(ligne);
				ligne = pColor+element;
			}else {
				ligne += (ligne.length() == pColor.length() ? "" : " ")+element;
			}
		}
		if(ligne != null)
			list.add(ligne);
		
		return list;
	}

	public EventsManager getEventsManager(){
		return this.eventsManager;
	}

	public WorldManager getWorldManager(){ return this.worldManager; }

	public DatabaseManager getDatabaseManager(){
		return this.databaseManager;
	}

	public GameManager getGameManager(){
		return this.gameManager;
	}

	public TeamDeletionManager getTeamDeletionManager(){
		return this.teamDeletionManager;
	}

}
