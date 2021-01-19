package fr.thedarven.scenarios.languages;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InventoryLanguageElement extends InventoryGUI implements AdminConfiguration {

	private static String SUB_DESCRIPTION_FORMAT = "§a► {description}";
	
	private final String languageShortName;
	private final String link;
	
	public InventoryLanguageElement(String name, String description, InventoryGUI parent, String languageShortName, String link) {
		super(name, description, null, 1, Material.SKULL_ITEM, parent, 0);
		this.languageShortName = languageShortName;
		this.link = link;

		if (parent instanceof InventoryLanguage) {
			InventoryLanguage inventoryParent = (InventoryLanguage) getParent();
			if (inventoryParent.getSelectedLanguage().equals(this.languageShortName)) {
				inventoryParent.setSelectedLanguage(this);
			}
		}
		reloadItem();
	}

	@Override
	protected List<String> getFormattedDescription() {
		List<String> returnArray = super.getFormattedDescription();
		if (getParent() instanceof InventoryLanguage) {
			InventoryLanguage parent = (InventoryLanguage) getParent();
			if (parent.getSelectedLanguage().equals(languageShortName)) {
				returnArray.add("");
				Map<String, String> params = new HashMap<>();
				params.put("description", LanguageBuilder.getContent("CONTENT", "selected", ((InventoryLanguage) getParent()).getSelectedLanguage(), true));
				returnArray.add(TextInterpreter.textInterpretation(SUB_DESCRIPTION_FORMAT, params));
			}
		}
		
		return returnArray;
	}
	
	/**
	 * Pour avoir le nom court
	 * 
	 * @return Le nom court
	 */
	final public String getLanguageShortName() {
		return this.languageShortName;
	}
	
	/**
	 * Pour avoir le lien
	 * 
	 * @return Le lien
	 */
	final public String getLink() {
		return this.link;
	}

	/**
	 * Pour recharger les items dans l'inventaire
	 */
	final public void reloadItem() {
		int exItem = getItem().hashCode();

		ItemStack head = Skull.getCustomSkull(link, getItem());
		ItemMeta headM = head.getItemMeta();
		headM.setDisplayName(getFormattedItemName());
		headM.setLore(getFormattedDescription());
		head.setItemMeta(headM);

		if (Objects.nonNull(this.getParent())) {
			this.getParent().updateChildItem(exItem, head, this);
		}
	}
	
}
