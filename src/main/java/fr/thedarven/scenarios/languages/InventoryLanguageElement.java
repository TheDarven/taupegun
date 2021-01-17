package fr.thedarven.scenarios.languages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryLanguageElement extends InventoryGUI {

	private static String SUB_DESCRIPTION_FORMAT = "§a► {description}";
	
	private String languageShortName;
	private String link;
	
	public InventoryLanguageElement(String pName, String pDescription, InventoryGUI pParent, String pLanguageShortName, String pLink) {
		super(pName, pDescription, null, 1, Material.SKULL_ITEM, pParent, 0);
		this.languageShortName = pLanguageShortName;
		this.link = pLink;

		if (getParent() instanceof InventoryLanguage) {
			InventoryLanguage parent = (InventoryLanguage) getParent();
			if (parent.getSelectedLanguage().equals(languageShortName)) {
				parent.setSelectedLanguage(this);
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
	public String getLanguageShortName() {
		return this.languageShortName;
	}
	
	/**
	 * Pour avoir le lien
	 * 
	 * @return Le lien
	 */
	public String getLink() {
		return this.link;
	}

	/**
	 * Pour recharger les items dans l'inventaire
	 */
	public void reloadItem() {
		int exItem = getItem().hashCode();

		ItemStack head = Skull.getCustomSkull(link, getItem());
		ItemMeta headM = head.getItemMeta();
		headM.setDisplayName(getFormattedItemName());
		headM.setLore(getFormattedDescription());
		head.setItemMeta(headM);

		if (this.getParent() != null)
			this.getParent().updateChildItem(exItem, head, this);
	}
	
}
