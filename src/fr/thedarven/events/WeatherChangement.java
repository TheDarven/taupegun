package fr.thedarven.events;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.thedarven.main.metier.EnumGameState;

public class WeatherChangement implements Listener {

	@EventHandler
	public void onWeatherChanges(WeatherChangeEvent e) {
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
			e.setCancelled(true);
			World currentWorld = e.getWorld();
			if(currentWorld.isThundering())
				currentWorld.setThundering(false);
			if(currentWorld.hasStorm())
				currentWorld.setStorm(false);
		}
	}
	
}