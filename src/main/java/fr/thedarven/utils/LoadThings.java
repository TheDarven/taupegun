package fr.thedarven.utils;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.TaupeGun;
import fr.thedarven.utils.languages.LanguageRegister;

public class LoadThings {

	public static void loadAll(TaupeGun plugin) {
		LanguageRegister.loadAllTranslations(plugin);
		InventoryGUI.setLanguage();
	}
}
