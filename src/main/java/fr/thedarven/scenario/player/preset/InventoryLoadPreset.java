package fr.thedarven.scenario.player.preset;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.player.preset.model.Preset;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryLoadPreset extends InventoryPresetAction implements AdminConfiguration {

    private static String LOAD_PRESET = "Le preset {presetName} a été chargé avec succès.";

    public InventoryLoadPreset(TaupeGun main, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, preset.getName(), "Charger le preset.", "MENU_PRESET_ITEM", Material.DIRT, preset, parent);
        updateLanguage(getLanguage());
        this.getParent().reloadInventory();
    }

    @Override
    public void updateLanguage(String language) {
        LOAD_PRESET = LanguageBuilder.getContent("PRESET", "load", language, true);

        super.updateLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languagePreset = LanguageBuilder.getLanguageBuilder("PRESET");
        languagePreset.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "load", LOAD_PRESET);

        return LanguageBuilder.getLanguageBuilder(getTranslationName());
    }

    protected String getFormattedItemName() {
        String name = getName();
        if (Objects.nonNull(this.preset)) {
            name = this.preset.getName();
        }
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return TextInterpreter.textInterpretation(ELEMENT_ITEM_NAME_FORMAT, params);
    }

    @Override
    protected void action(Player player, StatsPlayerTaupe pl) {
        Map<String, String> params = new HashMap<>();
        params.put("presetName", "§e§l" + this.preset.getName() + "§r§a");
        new ActionBar(TextInterpreter.textInterpretation("§a" + LOAD_PRESET, params)).sendActionBar(player);

        this.main.getScenariosManager().setCurrentConfiguration(this.preset);
    }
}
