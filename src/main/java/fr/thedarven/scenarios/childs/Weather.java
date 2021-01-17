package fr.thedarven.scenarios.childs;

import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Weather extends OptionBoolean {

	public Weather(InventoryGUI parent) {
		super("Météo", "Activer ou non les changements métérologiques.", "MENU_CONFIGURATION_OTHER_WEATHER",
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
