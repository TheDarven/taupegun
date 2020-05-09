package fr.thedarven.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.thedarven.events.commands.StartCommand;
import fr.thedarven.events.stats.RegeneratesHealth;
import fr.thedarven.events.stats.ThrowedArrow;
import fr.thedarven.main.TaupeGun;

public class EventsManager {
	
	public static void registerEvents(TaupeGun pl) {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Login(pl), pl);
		pm.registerEvents(new Walk(), pl);
		pm.registerEvents(new Damage(), pl);
		pm.registerEvents(new Death(), pl);
		pm.registerEvents(new Break(), pl);
		pm.registerEvents(new Eat(), pl);
		pm.registerEvents(new GoNether(), pl);
		pm.registerEvents(new MobsFixe(), pl);
		
		pm.registerEvents(new Drop(), pl);
		pm.registerEvents(new InventoryTeamInteract(pl), pl);
		pm.registerEvents(new ScenariosItemInteract(), pl);
		
		pm.registerEvents(new StartCommand(pl), pl);
		pm.registerEvents(new InvSee(pl), pl);
		pm.registerEvents(new Tchat(), pl);
		
		
		pm.registerEvents(new RegeneratesHealth(), pl);
		pm.registerEvents(new ThrowedArrow(), pl);
	}
}
