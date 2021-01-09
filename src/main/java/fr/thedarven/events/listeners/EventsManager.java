package fr.thedarven.events.listeners;

import fr.thedarven.events.listeners.*;
import fr.thedarven.main.metier.Manager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.thedarven.events.stats.RegeneratesHealth;
import fr.thedarven.events.stats.ThrowedArrow;
import fr.thedarven.TaupeGun;

public class EventsManager extends Manager {

	private Login login;

	private Death death;

	public EventsManager(TaupeGun main) {
		super(main);
		registerEvents();
	}

	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();

		this.login = new Login(this.main);
		pm.registerEvents(this.login, this.main);
		pm.registerEvents(new Walk(), this.main);
		pm.registerEvents(new Damage(this.main), this.main);
		this.death = new Death(this.main);
		pm.registerEvents(this.death, this.main);
		pm.registerEvents(new Break(), this.main);
		pm.registerEvents(new Eat(), main);
		pm.registerEvents(new GoNether(this.main), this.main);
		pm.registerEvents(new NerfMobs(), this.main);
		pm.registerEvents(new WeatherChangement(), this.main);
		
		pm.registerEvents(new Drop(), this.main);
		pm.registerEvents(new InventoryTeamInteract(this.main), this.main);
		pm.registerEvents(new ScenariosItemInteract(), this.main);
		
		pm.registerEvents(new InvSee(this.main), this.main);
		pm.registerEvents(new Tchat(this.main), this.main);

		// Stats
		pm.registerEvents(new RegeneratesHealth(), this.main);
		pm.registerEvents(new ThrowedArrow(), this.main);
	}

	public Login getLogin(){
		return this.login;
	}

	public Death getDeath(){
		return this.death;
	}
}
