package fr.thedarven.scenario.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.PlayerConfiguration;
import fr.thedarven.scenario.builders.InventoryAction;
import fr.thedarven.scenario.helper.AdminConfiguration;
import fr.thedarven.scenario.runnable.CreatePresetRunnable;
import fr.thedarven.utils.api.anvil.AnvilGUI;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryCreatePreset extends InventoryAction implements AdminConfiguration {

    private static String TOO_MANY_PRESET = "Vous ne pouvez pas avoir plus de 9 presets.";
    private static String DEFAULT_PRESET_NAME = "Nom du preset";
    public static String TOO_LONG_NAME_FORMAT = "Le nom du preset ne doit pas dépasser 16 caractères.";
    public static String NAME_ALREADY_USED_FORMAT = "Le nom est déjà utilisé pour un autre preset.";
    public static String PRESET_CREATE = "Le preset {presetName} a été créé avec succès.";

    private final PlayerConfiguration playerConfiguration;

    public InventoryCreatePreset(TaupeGun main, InventoryPlayersElementPreset parent, PlayerConfiguration playerConfiguration) {
        super(main, "Ajouter un preset", "Sauvegarder la configuration actuelle. Elle sera réutilisable après redémarrage du serveur.",
                "MENU_PRESET_ADD", 1, Material.BOOK, parent);
        this.playerConfiguration = playerConfiguration;
        updateLanguage(getLanguage());
    }

    @Override
    public void updateLanguage(String language) {
        TOO_MANY_PRESET = LanguageBuilder.getContent("PRESET", "tooManyPresets", language, true);
        DEFAULT_PRESET_NAME = LanguageBuilder.getContent("PRESET", "createPresetDefaultName", language, true);
        TOO_LONG_NAME_FORMAT = LanguageBuilder.getContent("PRESET", "nameTooLong", language, true);
        NAME_ALREADY_USED_FORMAT = LanguageBuilder.getContent("PRESET", "nameAlreadyUsed", language, true);
        PRESET_CREATE = LanguageBuilder.getContent("PRESET", "create", language, true);

        super.updateLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();

        LanguageBuilder languagePreset = LanguageBuilder.getLanguageBuilder("PRESET");
        languagePreset.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "tooManyPresets", TOO_MANY_PRESET);
        languagePreset.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "createPresetDefaultName", DEFAULT_PRESET_NAME);
        languagePreset.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameTooLong", TOO_LONG_NAME_FORMAT);
        languagePreset.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameAlreadyUsed", NAME_ALREADY_USED_FORMAT);
        languagePreset.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "create", PRESET_CREATE);

        return languageElement;
    }

    @Override
    protected void action(Player player, StatsPlayerTaupe pl) {
        if (playerConfiguration.isPresetAmountLimit()) {
            new ActionBar(ChatColor.RED + TOO_MANY_PRESET).sendActionBar(player);
            player.closeInventory();
        } else {
            createPresetAction(player, pl);
        }
    }

    /**
     * Permet à un joueur de créer un preset
     *
     * @param player Le Player qui souhaite créer un preset
     * @param pl Le PlayerTaupe du joueur
     */
    private void createPresetAction(Player player, StatsPlayerTaupe pl) {
        new AnvilGUI(this.main, player, (menu, text) -> {
            new CreatePresetRunnable(this.main, pl, player, this.playerConfiguration, text).runTask(this.main);
            return true;
        }).setInputName(DEFAULT_PRESET_NAME).open();
    }

}
