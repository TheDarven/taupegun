package fr.thedarven.scenarios.languages;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.childs.ScenariosVisible;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.TextInterpreter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryLanguage extends InventoryGUI implements AdminConfiguration {

	private static String SELECTING_LANGUAGE = "Vous avez sélectionné la langue {languageName}";

	private final String defaultSelectedLanguage;
	private InventoryLanguageElement selectedLanguage;

	public InventoryLanguage(InventoryGUI parent) {
		super("Langue", "Changer de langue.", "MENU_LANGUAGE",4, Material.SKULL_ITEM, parent,
				0, (byte) 3);
		this.selectedLanguage = null;
		this.defaultSelectedLanguage = "fr_FR";
	}


	@Override
	public void updateLanguage(String language) {
		SELECTING_LANGUAGE = LanguageBuilder.getContent("MENU_LANGUAGE", "selectingMessage", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "selectingMessage", SELECTING_LANGUAGE);
		
		return languageElement;
	}
	
	
	/**
	 * Pour avoir le nom court de la langue selectionnée
	 * 
	 * @return Le nom court de la langue selectionnée
	 */
	final public String getSelectedLanguage() {
		return Objects.isNull(this.selectedLanguage) ? this.defaultSelectedLanguage : this.selectedLanguage.getLanguageShortName();
	}
	
	/**
	 * Pour changer l'inventaire de la langue selectionnée
	 * 
	 * @param pSelectedLanguage Le nouveau inventaire langue selectionné
	 */
	final public void setSelectedLanguage(InventoryLanguageElement pSelectedLanguage) {
		this.selectedLanguage = pSelectedLanguage;
		reloadItem();
	}
	
	
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	final public void reloadItem() {
		int exItem = getItem().hashCode();
		
		String link = selectedLanguage.getLink();
		link = Objects.isNull(link) ? " " : link;
		
		ItemStack head = Skull.getCustomSkull(link, getItem());
		ItemMeta headM = head.getItemMeta();
		headM.setDisplayName(getFormattedItemName());
		head.setItemMeta(headM);

		if (Objects.nonNull(this.getParent())) {
			this.getParent().updateChildItem(exItem, head, this);
		}
	}
	
	
	
	/**
	 * Pour changer la langue selectionnée
	 * 
	 * @param selectedInventoryLanguage L'inventaire de la langue selectionnée
	 * @param player Le joueur qui a cliqué
	 */
	private void changeSelectedLanguage(InventoryLanguageElement selectedInventoryLanguage, Player player) {
		if (selectedInventoryLanguage == this.selectedLanguage)
			return;

		InventoryLanguageElement exSelectedLanguage = this.selectedLanguage;

		ScenariosVisible scenariosVisible = TaupeGun.getInstance().getScenariosManager().scenariosVisible;
		String exName =  scenariosVisible.getFormattedScenariosItemName();
		
		setSelectedLanguage(selectedInventoryLanguage);
		Bukkit.getOnlinePlayers().forEach(p -> scenariosVisible.reloadScenariosItem(p, exName));
		
		if (Objects.nonNull(exSelectedLanguage)) {
			exSelectedLanguage.reloadItem();
		}
		selectedInventoryLanguage.reloadItem();
		
		InventoryGUI.setLanguage();
		Bukkit.getOnlinePlayers().forEach(MessagesClass::TabMessage);
		
		Map<String, String> params = new HashMap<>();
		params.put("languageName", "§6" + this.selectedLanguage.getName() + "§e");
		String selectingLanguageMessage = TextInterpreter.textInterpretation("§e" + SELECTING_LANGUAGE, params);
		
		Title.sendActionBar(player, selectingLanguageMessage);
	}

}
