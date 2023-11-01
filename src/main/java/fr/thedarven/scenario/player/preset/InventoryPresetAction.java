package fr.thedarven.scenario.player.preset;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.player.preset.model.Preset;
import org.bukkit.Material;

public abstract class InventoryPresetAction extends ConfigurationInventory {

    protected final Preset preset;

    public InventoryPresetAction(TaupeGun main, String name, String description, String translationName, Material material, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, name, description, translationName, 1, material, parent);
        this.preset = preset;
    }

    public final Preset getPreset() {
        return this.preset;
    }

}
