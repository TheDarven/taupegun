package fr.thedarven.scenario.player.preset;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.InventoryDelete;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.player.preset.model.PlayerConfiguration;
import fr.thedarven.scenario.player.preset.model.Preset;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InventoryDeletePreset extends InventoryDelete implements AdminConfiguration {

    private static String DELETE_PRESET_FORMAT = "Votre preset {presetName} a été supprimé avec succès.";

    private final PlayerConfiguration playerConfiguration;
    private final Preset preset;

    public InventoryDeletePreset(TaupeGun main, InventoryPlayersElementPreset parent, PlayerConfiguration playerConfiguration, Preset preset) {
        super(main, parent, "Supprimer le preset", "MENU_PRESET_ITEM_DELETE", 1);
        this.playerConfiguration = playerConfiguration;
        this.preset = preset;
    }

    @Override
    public TreeInventory build() {
        super.build();
        this.getParent().reloadInventory();
        return this;
    }

    public Preset getPreset() {
        return this.preset;
    }

    @Override
    public void loadLanguage(String language) {
        DELETE_PRESET_FORMAT = LanguageBuilder.getContent("PRESET", "delete", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languagePreset = LanguageBuilder.getLanguageBuilder("PRESET");
        languagePreset.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "delete", DELETE_PRESET_FORMAT);
        return languageElement;
    }

    @Override
    protected void deleteElement(Player player) {
        Map<String, String> params = new HashMap<>();
        params.put("presetName", "§e§l" + this.preset.getName() + "§r§a");
        new ActionBar(TextInterpreter.textInterpretation("§a" + DELETE_PRESET_FORMAT, params)).sendActionBar(player);

        this.main.getScenariosManager().removePreset(this.preset, this.playerConfiguration);
        if (getParent() == null || !getParent().openInventory(player)) {
            player.closeInventory();
        }
    }

}
