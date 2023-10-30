package fr.thedarven.scenario.language;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryLanguage extends CustomInventory implements AdminConfiguration {

	private static String SELECTING_LANGUAGE = "Vous avez sélectionné la langue {languageName}";

	private final String defaultSelectedLanguage;
	private InventoryLanguageElement selectedLanguage;

	public InventoryLanguage(TaupeGun main, CustomInventory parent) {
		super(main, "Langue", "Changer de langue.", "MENU_LANGUAGE",4, Material.SKULL_ITEM, parent,
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

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, StatsPlayerTaupe pl) {
		ItemStack item = e.getCurrentItem();
		CustomInventory customInventory = this.children.get(item.hashCode());
		if (Objects.nonNull(customInventory) && customInventory instanceof InventoryLanguageElement) {
			changeSelectedLanguage((InventoryLanguageElement) customInventory, player);
			return;
		}
		openChildInventory(item, player, pl);
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
		
		setSelectedLanguage(selectedInventoryLanguage);
		
		if (Objects.nonNull(exSelectedLanguage)) {
			exSelectedLanguage.reloadItem();
		}
		selectedInventoryLanguage.reloadItem();
		
		CustomInventory.setLanguage();
		Bukkit.getOnlinePlayers().forEach(receiver -> this.main.getMessageManager().updateTabContent(receiver));
		
		Map<String, String> params = new HashMap<>();
		params.put("languageName", "§6" + this.selectedLanguage.getName() + "§e");
		String selectingLanguageMessage = TextInterpreter.textInterpretation("§e" + SELECTING_LANGUAGE, params);
		
		new ActionBar(selectingLanguageMessage).sendActionBar(player);
	}

}
