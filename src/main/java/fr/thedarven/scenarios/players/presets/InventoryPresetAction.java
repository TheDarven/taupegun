package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.Preset;
import fr.thedarven.scenarios.builders.InventoryAction;
import org.bukkit.Material;

public abstract class InventoryPresetAction extends InventoryAction {

    protected final Preset preset;

    public InventoryPresetAction(TaupeGun main, String name, String description, String translationName, Material material, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, name, description, translationName, 1, material, parent);
        this.preset = preset;
    }

    public final Preset getPreset() {
        return this.preset;
    }

}
