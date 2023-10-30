package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.builder.OptionBoolean;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Weather extends OptionBoolean {

	public Weather(TaupeGun main, CustomInventory parent) {
		super(main, "Météo", "Active ou non les changements météorologiques.", "MENU_CONFIGURATION_OTHER_WEATHER",
				Material.DAYLIGHT_DETECTOR, parent, 9, true);
	}
	
	/**
	 * Pour désactiver le changement de temps
	 * 
	 * @param e L'évènement de changement de temps
	 */
	@EventHandler
	final public void onWeatherChange(WeatherChangeEvent e) {
		if (this.value)
			return;

		if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
			e.setCancelled(e.toWeatherState());
			/* World currentWorld = e.getWorld();
			if(currentWorld.isThundering())
				currentWorld.setThundering(false); */
		}
	}
	
}
