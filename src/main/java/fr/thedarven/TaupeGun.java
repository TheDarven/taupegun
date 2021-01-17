package fr.thedarven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.database.DatabaseManager;
import fr.thedarven.events.commands.CommandManager;
import fr.thedarven.game.GameManager;
import fr.thedarven.items.ItemManager;
import fr.thedarven.utils.CraftManager;
import fr.thedarven.utils.languages.LanguageRegister;
import fr.thedarven.utils.teams.TeamDeletionManager;
import fr.thedarven.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import fr.thedarven.scenarios.ScenariosManager;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.api.SqlConnection;
import fr.thedarven.utils.api.scoreboard.ScoreboardManager;
import fr.thedarven.events.listeners.ListenerManager;
import fr.thedarven.statsgame.RestGame;

public class TaupeGun extends JavaPlugin implements Listener{	

	public static TaupeGun instance;

	public boolean development = false;
	
	public SqlConnection sql;
	
	private ScenariosManager scenariosManager;
	private ScoreboardManager scoreboardManager;
	private ListenerManager listenerManager;
	private CommandManager commandManager;
	private WorldManager worldManager;
	private DatabaseManager databaseManager;
	private CraftManager craftManager;
	private GameManager gameManager;
	private TeamDeletionManager teamDeletionManager;
	private ScenariosManager inventoryRegister;
	private ItemManager itemManager;
	
	public static TaupeGun getInstance(){
		return instance;
	}
	
	@Override
	public void onEnable(){
		instance = this;

		LanguageRegister.loadAllTranslations(this);
		InventoryGUI.setLanguage();

		scoreboardManager = new ScoreboardManager(this);
		inventoryRegister = new ScenariosManager(this);
		listenerManager = new ListenerManager(this);
		commandManager = new CommandManager(this);

		this.saveDefaultConfig();

		worldManager = new WorldManager(this);
		worldManager.buildLobby();

		databaseManager = new DatabaseManager(this);
		craftManager = new CraftManager(this);
		itemManager = new ItemManager(this);
		
		for(Player p: Bukkit.getOnlinePlayers()){
			this.listenerManager.getPlayerJoinQuitListener().loginAction(p);

			for(PotionEffect potion : p.getActivePotionEffects())
				p.removePotionEffect(potion.getType());

			p.setHealth(20);
			p.setMaxHealth(20.0);
			p.setFoodLevel(20);
			p.setExhaustion(5F);
			p.setExp(0L+0F);
			p.setLevel(0);
		}
		new RestGame(this);

		gameManager = new GameManager(this);

		teamDeletionManager = new TeamDeletionManager(this);
	}
	
	@Override
	public void onDisable(){
		for(Player p: Bukkit.getOnlinePlayers()){
			if(!inventoryRegister.coordonneesVisibles.getValue())
				DisableF3.enableF3(p);

			this.listenerManager.getPlayerJoinQuitListener().leaveAction(p);
			
			UtilsClass.clearPlayer(p);
		}
		if (this.databaseManager.getGameId() != 0)
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

	public ListenerManager getListenerManager(){
		return this.listenerManager;
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

	public ScenariosManager getInventoryRegister() { return this.inventoryRegister; }

	public ScoreboardManager getScoreboardManager() {
		 return this.scoreboardManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}
}
