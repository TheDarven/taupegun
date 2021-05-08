package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.Preset;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryRenamePreset extends InventoryPresetAction implements AdminConfiguration {

    public InventoryRenamePreset(TaupeGun main, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, "Renommer le preset", "Permet de renommer votre preset.", "MENU_PRESET_RENAME",
                Material.NAME_TAG, preset, parent);
        this.getParent().reloadInventory();
    }

    @Override
    protected void action(Player player, PlayerTaupe pl) {
        // TODO Rename the preset
    }

}
