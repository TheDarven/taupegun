package fr.thedarven.events.listeners;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.listeners.stats.RegeneratesHealthListener;
import fr.thedarven.events.listeners.stats.ThrewArrowListener;
import fr.thedarven.models.Manager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager extends Manager {

	private PlayerJoinQuitListener playerJoinQuitListener;
	private PlayerDeathListener deathListener;

	public ListenerManager(TaupeGun main) {
		super(main);
		registerEvents();
	}

	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();

		this.playerJoinQuitListener = new PlayerJoinQuitListener(this.main);
		pm.registerEvents(this.playerJoinQuitListener, this.main);
		pm.registerEvents(new PlayerMoveListener(this.main), this.main);
		pm.registerEvents(new EntityDamageListener(this.main), this.main);
		this.deathListener = new PlayerDeathListener(this.main);
		pm.registerEvents(this.deathListener, this.main);
		pm.registerEvents(new BreakBlockListener(), this.main);
		pm.registerEvents(new PlayerItemConsumeListener(), main);
		pm.registerEvents(new DimensionListener(this.main), this.main);
		pm.registerEvents(new WeatherChangeListener(), this.main);
		
		pm.registerEvents(new PlayerDropListener(), this.main);
		pm.registerEvents(new InventoryClickListener(this.main), this.main);
		pm.registerEvents(new TeamsInventoryClickListener(), this.main);
		
		pm.registerEvents(new PlayerInvSeeListener(this.main), this.main);
		pm.registerEvents(new PlayerChatListener(this.main), this.main);

		// Stats
		pm.registerEvents(new RegeneratesHealthListener(), this.main);
		pm.registerEvents(new ThrewArrowListener(), this.main);
	}

	public PlayerJoinQuitListener getPlayerJoinQuitListener(){
		return this.playerJoinQuitListener;
	}

	public PlayerDeathListener getDeathListener(){
		return this.deathListener;
	}
}
