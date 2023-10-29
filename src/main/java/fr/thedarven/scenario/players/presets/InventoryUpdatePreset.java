package fr.thedarven.scenario.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.Preset;
import fr.thedarven.scenario.helper.AdminConfiguration;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InventoryUpdatePreset extends InventoryPresetAction implements AdminConfiguration {

    public static String UPDATE_PRESET = "Le preset {presetName} a été modifié avec succès.";

    public InventoryUpdatePreset(TaupeGun main, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, "Modifier le preset", "Permet de remplacer le preset sauvegardé par la configuration actuelle.",
                "MENU_PRESET_UPDATE", Material.ANVIL, preset, parent);
        updateLanguage(getLanguage());
        this.getParent().reloadInventory();
    }

    @Override
    public void updateLanguage(String language) {
        UPDATE_PRESET = LanguageBuilder.getContent("PRESET", "update", language, true);

        super.updateLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();

        LanguageBuilder languagePreset = LanguageBuilder.getLanguageBuilder("PRESET");
        languagePreset.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "update", UPDATE_PRESET);

        return languageElement;
    }

    @Override
    protected void action(Player player, StatsPlayerTaupe pl) {
        Map<String, String> params = new HashMap<>();
        params.put("presetName", "§e§l" + this.preset.getName() + "§r§a");
        new ActionBar(TextInterpreter.textInterpretation("§a" + UPDATE_PRESET, params)).sendActionBar(player);

        this.preset.setValues(this.main.getScenariosManager().getCurrentConfiguration());
    }

}
