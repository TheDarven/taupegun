package fr.thedarven;

import fr.thedarven.database.DatabaseManager;
import fr.thedarven.events.commands.CommandManager;
import fr.thedarven.events.listeners.ListenerManager;
import fr.thedarven.game.GameManager;
import fr.thedarven.items.ItemManager;
import fr.thedarven.kits.KitManager;
import fr.thedarven.players.PlayerManager;
import fr.thedarven.scenarios.ScenariosManager;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.statsgame.RestGame;
import fr.thedarven.teams.TeamManager;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.api.scoreboard.ScoreboardManager;
import fr.thedarven.utils.languages.LanguageRegister;
import fr.thedarven.utils.manager.CraftManager;
import fr.thedarven.utils.manager.TeamDeletionManager;
import fr.thedarven.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TaupeGun extends JavaPlugin implements Listener{	

	public static TaupeGun instance;

	public boolean development = false;

	private ScoreboardManager scoreboardManager;
	private KitManager kitManager;
	private ListenerManager listenerManager;
	private CommandManager commandManager;
	private WorldManager worldManager;
	private DatabaseManager databaseManager;
	private CraftManager craftManager;
	private GameManager gameManager;
	private TeamDeletionManager teamDeletionManager;
	private ScenariosManager scenariosManager;
	private ItemManager itemManager;
	private PlayerManager playerManager;
	private TeamManager teamManager;
	
	public static TaupeGun getInstance(){
		return instance;
	}
	
	@Override
	public void onEnable(){
		instance = this;

		LanguageRegister.loadAllTranslations(this);
		InventoryGUI.setLanguage();

		this.itemManager = new ItemManager(this);
		this.scoreboardManager = new ScoreboardManager(this);
		this.scenariosManager = new ScenariosManager(this);
		this.kitManager = new KitManager(this);
		this.listenerManager = new ListenerManager(this);
		this.commandManager = new CommandManager(this);

		this.saveDefaultConfig();

		this.worldManager = new WorldManager(this);
		this.worldManager.buildLobby();

		this.databaseManager = new DatabaseManager(this);

		this.craftManager = new CraftManager(this);
		this.craftManager.loadCrafts();

		this.playerManager = new PlayerManager(this);
		this.teamManager = new TeamManager(this);

		Bukkit.getOnlinePlayers().forEach(player -> this.playerManager.resetPlayerData(player));

		new RestGame(this);

		this.gameManager = new GameManager(this);
		this.teamDeletionManager = new TeamDeletionManager(this);
	}
	
	@Override
	public void onDisable(){
		for (Player p: Bukkit.getOnlinePlayers()) {
			if (!scenariosManager.coordonneesVisibles.getValue()) {
				DisableF3.enableF3(p);
			}

			this.listenerManager.getPlayerJoinQuitListener().leaveAction(p);

			getPlayerManager().clearPlayer(p);
		}
		if (this.databaseManager.getGameId() != 0) {
			databaseManager.updateGameDuration();
		}
		this.scenariosManager.savePlayersConfiguration();
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

	public ScenariosManager getScenariosManager() { return this.scenariosManager; }

	public ScoreboardManager getScoreboardManager() {
		 return this.scoreboardManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public TeamManager getTeamManager() {
		return teamManager;
	}

	public KitManager getKitManager() { return this.kitManager; }
}
