package fr.thedarven.configuration.builders.kits;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryDelete;
import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryDeleteKits extends InventoryDelete {
	
	private static String DELETE_KIT_FORMAT = "Le kit {kitName} a été supprimé avec succès.";
	
	public InventoryDeleteKits(InventoryGUI pInventoryGUI) {
		super(pInventoryGUI, "Supprimer le kit", "MENU_KIT_ITEM_DELETE", 9);
	}
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		DELETE_KIT_FORMAT = LanguageBuilder.getContent("KIT", "delete", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "delete", DELETE_KIT_FORMAT);
		
		return languageElement;
	}
	
	
	
	/**
	 * Pour supprimer un kit
	 */
	protected void deleteElement(Player p) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("kitName", "§e§l"+this.getParent().getName()+"§r§c");
		Title.sendActionBar(p, TextInterpreter.textInterpretation("§c"+DELETE_KIT_FORMAT, params));
		
		InventoryKitsElement.removeKit(getParent().getName());
		p.openInventory(InventoryRegister.kits.getInventory());
	}
}
