package fr.thedarven.scenario.language;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
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

	private InventoryLanguageElement selectedLanguage;

	public InventoryLanguage(TaupeGun main, CustomInventory parent) {
		super(main, "Langue", "Changer de langue.", "MENU_LANGUAGE",4, Material.SKULL_ITEM, parent,
				0, (byte) 3);
		this.selectedLanguage = null;
	}


	@Override
	public void updateLanguage(String language) {
		SELECTING_LANGUAGE = LanguageBuilder.getContent("MENU_LANGUAGE", "selectingMessage", language, true);
		
		super.updateLanguage(language);

		this.children.values().stream()
				.filter(child -> child instanceof InventoryLanguageElement && ((InventoryLanguageElement) child).getLanguageShortName().equalsIgnoreCase(language))
				.map(child -> (InventoryLanguageElement) child)
				.findFirst()
				.ifPresent(child -> {
					InventoryLanguageElement exSelectedLanguage = this.selectedLanguage;
					if (Objects.nonNull(exSelectedLanguage) && exSelectedLanguage != child) {
						exSelectedLanguage.reloadItem();
						this.selectedLanguage.reloadItem();
						this.selectedLanguage = child;
					}
				});

		this.reloadItem();
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "selectingMessage", SELECTING_LANGUAGE);
		
		return languageElement;
	}

	
	/**
	 * Pour changer l'inventaire de la langue selectionnée
	 * 
	 * @param pSelectedLanguage Le nouveau inventaire langue selectionné
	 */
	final public void setSelectedLanguageInventory(InventoryLanguageElement pSelectedLanguage) {
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
			if (customInventory == this.selectedLanguage) {
				return;
			}
			this.main.getLanguageManager().setLanguage(((InventoryLanguageElement) customInventory).getLanguageShortName());

			Map<String, String> params = new HashMap<>();
			params.put("languageName", "§6" + this.selectedLanguage.getName() + "§e");
			String selectingLanguageMessage = TextInterpreter.textInterpretation("§e" + SELECTING_LANGUAGE, params);
			new ActionBar(selectingLanguageMessage).sendActionBar(player);



			// changeSelectedLanguage((InventoryLanguageElement) customInventory, player);
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
		/* if (selectedInventoryLanguage == this.selectedLanguage)
			return; */

		/* InventoryLanguageElement exSelectedLanguage = this.selectedLanguage;
		
		setSelectedLanguage(selectedInventoryLanguage);
		
		if (Objects.nonNull(exSelectedLanguage)) {
			exSelectedLanguage.reloadItem();
		}
		selectedInventoryLanguage.reloadItem(); */

		
		/* CustomInventory.setLanguage();
		Bukkit.getOnlinePlayers().forEach(receiver -> this.main.getMessageManager().updateTabContent(receiver));
		
		Map<String, String> params = new HashMap<>();
		params.put("languageName", "§6" + this.selectedLanguage.getName() + "§e");
		String selectingLanguageMessage = TextInterpreter.textInterpretation("§e" + SELECTING_LANGUAGE, params);
		
		new ActionBar(selectingLanguageMessage).sendActionBar(player); */
	}

}
