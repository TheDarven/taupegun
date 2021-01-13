package fr.thedarven.configuration.builders.childs;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;
import fr.thedarven.models.enums.EnumGameState;

public class Weather extends OptionBoolean {

	public Weather(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue);
	}
	
	public Weather(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pValue);
	}
	
	/**
	 * Pour désactiver le changement de temps
	 * 
	 * @param e L'évènement de changement de temps
	 */
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		if (EnumGameState.isCurrentState(EnumGameState.GAME) && !this.value) {
			e.setCancelled(e.toWeatherState());
			/* World currentWorld = e.getWorld();
			if(currentWorld.isThundering())
				currentWorld.setThundering(false); */
		}
	}
	
}
