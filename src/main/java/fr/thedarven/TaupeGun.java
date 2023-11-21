package fr.thedarven;

import fr.thedarven.database.DatabaseManager;
import fr.thedarven.events.command.CommandManager;
import fr.thedarven.events.listener.ListenerManager;
import fr.thedarven.game.GameManager;
import fr.thedarven.kit.KitManager;
import fr.thedarven.player.PlayerManager;
import fr.thedarven.scenario.ScenariosManager;
import fr.thedarven.stats.model.dto.GameDto;
import fr.thedarven.team.TeamManager;
import fr.thedarven.utils.Metrics;
import fr.thedarven.utils.api.DisableF3;
import fr.thedarven.utils.api.scoreboard.ScoreboardManager;
import fr.thedarven.utils.languages.TranslationManager;
import fr.thedarven.utils.manager.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TaupeGun extends JavaPlugin implements Listener{

	public static TaupeGun instance;

	public boolean development = false;

	private Metrics metrics;
	private LanguageManager languageManager;
	private TranslationManager translationManager;
	private MessageManager messageManager;
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
	private PlayerManager playerManager;
	private TeamManager teamManager;

	public static TaupeGun getInstance(){
		return instance;
	}

	@Override
	public void onEnable(){
		instance = this;

		this.teamManager = new TeamManager(this);

		this.metrics = new Metrics(this, 11400);

		this.languageManager = new LanguageManager(this);

		this.translationManager = new TranslationManager(this);
		this.translationManager.loadAllTranslations(this);

		this.messageManager = new MessageManager(this);

		this.kitManager = new KitManager(this);

		this.scoreboardManager = new ScoreboardManager(this);
		this.scenariosManager = new ScenariosManager(this);
		this.worldManager = new WorldManager(this);
		this.worldManager.buildLobby();
		this.kitManager.initDefaultKits();
		this.listenerManager = new ListenerManager(this);
		this.commandManager = new CommandManager(this);

		this.saveDefaultConfig();

		this.databaseManager = new DatabaseManager(this);

		this.craftManager = new CraftManager(this);
		this.craftManager.loadCrafts();

		this.playerManager = new PlayerManager(this);

		Bukkit.getOnlinePlayers().forEach(player -> this.playerManager.resetPlayerData(player));

		new GameDto(this);

		this.gameManager = new GameManager(this);
		this.teamDeletionManager = new TeamDeletionManager(this);
	}

	@Override
	public void onDisable(){
		for (Player p: Bukkit.getOnlinePlayers()) {
			if (this.scenariosManager != null && !this.scenariosManager.coordonneesVisibles.getValue()) {
				new DisableF3().enableF3(p);
			}

			if (this.listenerManager != null) {
				this.listenerManager.getPlayerJoinQuitListener().leaveAction(p);
			}

			getPlayerManager().clearPlayer(p);
		}

		if (this.databaseManager != null && this.databaseManager.getGameId() != 0) {
			databaseManager.updateGameDuration();
		}

		if (this.scenariosManager != null) {
			this.scenariosManager.savePlayersConfiguration();
		}
	}

	public LanguageManager getLanguageManager() {
		return this.languageManager;
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

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public TeamManager getTeamManager() {
		return teamManager;
	}

	public KitManager getKitManager() { return this.kitManager; }

	public MessageManager getMessageManager() {
		return this.messageManager;
	}
}
