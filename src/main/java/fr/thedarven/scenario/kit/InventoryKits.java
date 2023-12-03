package fr.thedarven.scenario.kit;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.kit.KitCreateEvent;
import fr.thedarven.events.event.team.TeamDeleteEvent;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.kit.KitManager;
import fr.thedarven.kit.model.Kit;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.InventoryIncrement;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Optional;

public class InventoryKits extends InventoryIncrement implements AdminConfiguration {

    public static String TOO_LONG_NAME_FORMAT = "Le nom du kit ne doit pas dépasser 16 caractères.";
    public static String NAME_ALREADY_USED_FORMAT = "Le nom est déjà utilisé pour un autre kit.";
    public static String KIT_CREATE = "Le kit {kitName} a été créé avec succès.";
    public static String TOO_MANY_KITS = "Vous ne pouvez pas créer plus de {maxKit} kits.";

    public InventoryKits(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Kits", "Menu des kits.", "MENU_KIT", 2, Material.ENDER_CHEST, parent, 4);
    }

    @Override
    public void loadLanguage(String language) {
        TOO_LONG_NAME_FORMAT = LanguageBuilder.getContent("KIT", "nameTooLong", language, true);
        NAME_ALREADY_USED_FORMAT = LanguageBuilder.getContent("KIT", "nameAlreadyUsed", language, true);
        KIT_CREATE = LanguageBuilder.getContent("KIT", "create", language, true);
        TOO_MANY_KITS = LanguageBuilder.getContent("KIT", "tooManyKits", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "nameTooLong", TOO_LONG_NAME_FORMAT);
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "nameAlreadyUsed", NAME_ALREADY_USED_FORMAT);
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "create", KIT_CREATE);
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "tooManyKits", TOO_MANY_KITS);
        return languageElement;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKitCreate(KitCreateEvent event) {
        if (!EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            return;
        }

        InventoryKitsElement kitInventory = new InventoryKitsElement(this.main, this, event.getKit());
        kitInventory.build();
        new InventoryDeleteKits(this.main, kitInventory, event.getKit()).build();

        this.refreshInventoryItems();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeamDelete(TeamDeleteEvent event) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            this.refreshInventoryItems();
        }
    }

    @Override
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();
        removeChildrenItems();

        int position = 0;
        for (TreeInventory child : getChildren()) {
            if (child instanceof InventoryKitsElement) {
                updateChildPositionItem(child, position);
                position++;
            } else if (child instanceof InventoryCreateKit && this.main.getKitManager().countKits() < KitManager.MAX_KIT_AMOUNT) {
                updateChildPositionItem(child, this.main.getKitManager().countKits());
            }
        }
    }

    public Optional<InventoryKitsElement> getInventoryOfKit(Kit kit) {
        return this.getChildren().stream()
                .filter(inventory -> inventory instanceof InventoryKitsElement && ((InventoryKitsElement) inventory).getKit() == kit)
                .map(inventory -> (InventoryKitsElement) inventory)
                .findFirst();
    }

}
