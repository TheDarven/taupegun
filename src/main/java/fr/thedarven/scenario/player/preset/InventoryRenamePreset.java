package fr.thedarven.scenario.player.preset;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.player.preset.model.Preset;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryRenamePreset extends InventoryPresetAction implements AdminConfiguration {

    public InventoryRenamePreset(TaupeGun main, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, "Renommer le preset", "En développement.", "MENU_PRESET_RENAME",
                Material.NAME_TAG, preset, parent);
    }

    @Override
    public void onClickIn(Player player, PlayerTaupe pl) {
        // TODO Rename the preset
    }

}
