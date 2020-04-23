package fr.thedarven.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.thedarven.events.commands.StartCommand;
import fr.thedarven.main.TaupeGun;

public class EventsManager {
	
	public static void registerEvents(TaupeGun pl) {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Login(pl), pl);
		pm.registerEvents(new Walk(pl), pl);
		pm.registerEvents(new Damage(pl), pl);
		pm.registerEvents(new Death(pl), pl);
		pm.registerEvents(new Break(pl), pl);
		pm.registerEvents(new Eat(pl), pl);
		pm.registerEvents(new GoNether(pl), pl);
		pm.registerEvents(new MobsFixe(pl), pl);
		
		pm.registerEvents(new Drop(pl), pl);
		pm.registerEvents(new InventoryTeamInteract(pl), pl);
		pm.registerEvents(new ScenariosItemInteract(pl), pl);
		
		pm.registerEvents(new StartCommand(pl), pl);
		pm.registerEvents(new InvSee(pl), pl);
		pm.registerEvents(new Tchat(pl), pl);
	}
}
