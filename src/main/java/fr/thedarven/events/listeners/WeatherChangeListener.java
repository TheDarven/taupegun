package fr.thedarven.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.thedarven.models.EnumGameState;

public class WeatherChangeListener implements Listener {

	@EventHandler
	public void onWeatherChanges(WeatherChangeEvent e) {
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
			e.setCancelled(e.toWeatherState());
			/* World currentWorld = e.getWorld();
			if(currentWorld.isThundering())
				currentWorld.setThundering(false); */
		}
	}
	
}
