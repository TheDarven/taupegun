package fr.thedarven.scenario.kit;

import java.util.HashMap;
import java.util.Map;

import fr.thedarven.TaupeGun;
import fr.thedarven.kit.model.Kit;
import fr.thedarven.scenario.builder.InventoryDelete;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.api.titles.ActionBar;
import org.bukkit.entity.Player;

import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;

public class InventoryDeleteKits extends InventoryDelete implements AdminConfiguration {
	
	private static String DELETE_KIT_FORMAT = "Le kit {kitName} a été supprimé avec succès.";
	private final Kit kit;
	
	public InventoryDeleteKits(TaupeGun main, CustomInventory parent, Kit kit) {
		super(main, parent, "Supprimer le kit", "MENU_KIT_ITEM_DELETE", 9);
		this.kit = kit;
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
		new ActionBar(TextInterpreter.textInterpretation("§c" + DELETE_KIT_FORMAT, params)).sendActionBar(player);

		this.main.getKitManager().removeKit(this.kit);
		player.openInventory(this.main.getScenariosManager().kitsMenu.getInventory());
	}
}
