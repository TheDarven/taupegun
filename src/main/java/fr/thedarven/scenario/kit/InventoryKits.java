package fr.thedarven.scenario.kit;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.InventoryIncrement;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;

public class InventoryKits extends InventoryIncrement implements AdminConfiguration {

    public static String TOO_LONG_NAME_FORMAT = "Le nom du kit ne doit pas dépasser 16 caractères.";
    public static String NAME_ALREADY_USED_FORMAT = "Le nom est déjà utilisé pour un autre kit.";
    public static String KIT_CREATE = "Le kit {kitName} a été créé avec succès.";

    public InventoryKits(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Kits", "Menu des kits.", "MENU_KIT", 2, Material.ENDER_CHEST, parent, 4);
    }

    @Override
    public void loadLanguage(String language) {
        TOO_LONG_NAME_FORMAT = LanguageBuilder.getContent("KIT", "nameTooLong", language, true);
        NAME_ALREADY_USED_FORMAT = LanguageBuilder.getContent("KIT", "nameAlreadyUsed", language, true);
        KIT_CREATE = LanguageBuilder.getContent("KIT", "create", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "nameTooLong", TOO_LONG_NAME_FORMAT);
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "nameAlreadyUsed", NAME_ALREADY_USED_FORMAT);
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "create", KIT_CREATE);
        return languageElement;
    }


    @Override
    public void reloadInventory() {
        this.getChildren().forEach(this::removeChildItem);

        int i = 0;
        for (TreeInventory treeInventory : getChildren()) {
            if (treeInventory instanceof InventoryKitsElement) {
                updateChildPositionItem(treeInventory, i);
                i++;
            } else if (countChildren() < 10) {
                updateChildPositionItem(treeInventory, countChildren() - 1);
            }
        }
    }

}
