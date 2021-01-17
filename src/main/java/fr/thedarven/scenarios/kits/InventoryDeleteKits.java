package fr.thedarven.scenarios.kits;

import java.util.HashMap;
import java.util.Map;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryDelete;
import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.entity.Player;

import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryDeleteKits extends InventoryDelete {
	
	private static String DELETE_KIT_FORMAT = "Le kit {kitName} a été supprimé avec succès.";
	
	public InventoryDeleteKits(InventoryGUI pInventoryGUI) {
		super(pInventoryGUI, "Supprimer le kit", "MENU_KIT_ITEM_DELETE", 9);
	}
	
	
	
	@Override
	public void updateLanguage(String language) {
		DELETE_KIT_FORMAT = LanguageBuilder.getContent("KIT", "delete", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "delete", DELETE_KIT_FORMAT);
		
		return languageElement;
	}
	
	
	
	@Override
	protected void deleteElement(Player player) {
		Map<String, String> params = new HashMap<>();
		params.put("kitName", "§e§l" + getParent().getName() + "§r§c");
		Title.sendActionBar(player, TextInterpreter.textInterpretation("§c" + DELETE_KIT_FORMAT, params));
		
		InventoryKitsElement.removeKit(getParent().getName());
		player.openInventory(TaupeGun.getInstance().getInventoryRegister().kitsMenu.getInventory());
	}
}
