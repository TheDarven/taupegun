package fr.thedarven.scenarios.kits;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.InventoryIncrement;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.runnable.CreateKitRunnable;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class InventoryKits extends InventoryIncrement implements AdminConfiguration {
	
	public static String TOO_LONG_NAME_FORMAT = "Le nom du kit ne doit pas dépasser 16 caractères.";
	public static String NAME_ALREADY_USED_FORMAT = "Le nom est déjà utilisé pour un autre kit.";
	public static String KIT_CREATE = "Le kit {kitName} a été créé avec succès.";
	public static String CREATE_KIT_NAME_FORMAT = "Nom du kit";
	
	public InventoryKits(TaupeGun main, InventoryGUI parent) {
		super(main, "Kits", "Menu des kits.", "MENU_KIT", 2, Material.ENDER_CHEST, parent, 4);
	}







	@Override
	public void updateLanguage(String language) {
		TOO_LONG_NAME_FORMAT = LanguageBuilder.getContent("KIT", "nameTooLong", language, true);
		NAME_ALREADY_USED_FORMAT = LanguageBuilder.getContent("KIT", "nameAlreadyUsed", language, true);
		KIT_CREATE = LanguageBuilder.getContent("KIT", "create", language, true);
		CREATE_KIT_NAME_FORMAT = LanguageBuilder.getContent("KIT", "createNameMessage", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameTooLong", TOO_LONG_NAME_FORMAT);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameAlreadyUsed", NAME_ALREADY_USED_FORMAT);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "create", KIT_CREATE);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "createNameMessage", CREATE_KIT_NAME_FORMAT);
		
		return languageElement;
	}



	@Override
	public void reloadInventory() {
		for (InventoryGUI inv : getChildsValue()) {
			inv.getParent().removeItem(inv);
		}

		int i = 0;
		for (InventoryGUI inv : getChildsValue()) {
			if (inv instanceof InventoryKitsElement) {
				modifiyPosition(inv, i);
				i++;
			} else if (getChilds().size() < 10){
				modifiyPosition(inv,getChilds().size() - 1);
			}
		}
	}



	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		if (Objects.equals(e.getCurrentItem(), this.main.getScenariosManager().addKit.getItem())) {
			final InventoryKits parent = this;

			new AnvilGUI(this.main, player, (menu, text) -> {
				pl.setCreateKitName(text);
				new CreateKitRunnable(this.main, player, pl, parent).runTask(this.main);
				return true;
			}).setInputName(CREATE_KIT_NAME_FORMAT).open();
			return;
		}

		openChildInventory(e.getCurrentItem(), player, pl);
	}
	
}
