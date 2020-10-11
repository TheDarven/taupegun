package fr.thedarven.events;

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
		this.login = new Login(main);
		pm.registerEvents(this.login, main);
		pm.registerEvents(new Walk(), main);
		pm.registerEvents(new Damage(this.main), main);
		this.death = new Death(main);
		pm.registerEvents(this.death, main);
		pm.registerEvents(new Break(), main);
		pm.registerEvents(new Eat(), main);
		pm.registerEvents(new GoNether(this.main), main);
		pm.registerEvents(new MobsFixe(), main);
		pm.registerEvents(new WeatherChangement(), main);
		
		pm.registerEvents(new Drop(), main);
		pm.registerEvents(new InventoryTeamInteract(main), main);
		pm.registerEvents(new ScenariosItemInteract(), main);
		
		pm.registerEvents(new InvSee(main), main);
		pm.registerEvents(new Tchat(), main);
		
		// Stats
		pm.registerEvents(new RegeneratesHealth(), main);
		pm.registerEvents(new ThrowedArrow(), main);
	}

	public Login getLogin(){
		return this.login;
	}

	public Death getDeath(){
		return this.death;
	}
}
