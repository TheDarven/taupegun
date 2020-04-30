package fr.thedarven.configuration.builders.languages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryLanguageElement extends InventoryGUI{

	private static String SUB_DESCRIPTION_FORMAT = "§a► {description}";
	
	private String languageShortName;
	private String link;
	
	public InventoryLanguageElement(String pName, String pDescription, InventoryGUI pParent, String pLanguageShortName, String pLink) {
		super(pName, pDescription, null, 1, Material.SKULL_ITEM, pParent, 0);
		this.languageShortName = pLanguageShortName;
		this.link = pLink;

		if(getParent() instanceof InventoryLanguage) {
			InventoryLanguage parent = (InventoryLanguage) getParent();
			if(parent.getSelectedLanguage().equals(languageShortName)) {
				parent.setSelectedLanguage(this);
			}
		}
		reloadItem();
	}
	
	/**
	 * Pour la description formatée
	 */
	protected ArrayList<String> getFormattedDescription() {
		ArrayList<String> returnArray = super.getFormattedDescription();
		returnArray.add("");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("description", LanguageBuilder.getContent("CONTENT", "selected", InventoryRegister.language.getSelectedLanguage(), true));
		returnArray.add(TextInterpreter.textInterpretation(SUB_DESCRIPTION_FORMAT, params));
		
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
		if(getParent() instanceof InventoryLanguage) {
			InventoryLanguage parent = (InventoryLanguage) getParent();
			if(parent.getSelectedLanguage().equals(languageShortName)) {
				headM.setLore(getFormattedDescription());
			}else{
				headM.setLore(new ArrayList<String>());
			}
		}
		head.setItemMeta(headM);
		
		updateItem(exItem, head);
	}
	
}