package fr.thedarven.scenarios.teams;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryTeamsParameters extends InventoryGUI implements AdminConfiguration {

protected static ArrayList<InventoryTeamsParameters> inventory = new ArrayList<>();
	
	private static String CHANGE_NAME = "Changer le nom";
	private static String CHANGE_COLOR = "Changer la couleur";

	public InventoryTeamsParameters(TaupeGun main, InventoryGUI parent) {
		super(main, "Paramètres", null, "MENU_TEAM_ITEM_PARAMETER", 1, Material.REDSTONE_COMPARATOR, parent, 22);
		inventory.add(this);
		
		updateLanguage(getLanguage());
	}
	
	
	
	
	
	
	@Override
	public void updateLanguage(String language) {
		CHANGE_NAME = LanguageBuilder.getContent("TEAM", "changeName", language, true);
		CHANGE_COLOR = LanguageBuilder.getContent("TEAM", "changeColor", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "changeName", CHANGE_NAME);
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "changeColor", CHANGE_COLOR);
		
		return languageElement;
	}






	@Override
	protected void reloadItems() {
		super.reloadItems();
		reloadItem();
	}
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	final protected void reloadItem() {
		if (Objects.isNull(inventory) || Objects.isNull(getInventory()))
			return;

		ItemStack name = new ItemStack(Material.PAPER, 1);
		ItemMeta nameM = name.getItemMeta();
		nameM.setDisplayName("§e" + CHANGE_NAME);
		name.setItemMeta(nameM);
		getInventory().setItem(0, name);

		ItemStack color = new ItemStack(Material.BANNER, 1, (byte) 15);
		ItemMeta colorM = color.getItemMeta();
		colorM.setDisplayName("§e" + CHANGE_COLOR);
		color.setItemMeta(colorM);
		getInventory().setItem(1, color);
	}
	
}
