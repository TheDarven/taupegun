package fr.thedarven.scenario.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.Preset;
import fr.thedarven.scenario.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryRenamePreset extends InventoryPresetAction implements AdminConfiguration {

    public InventoryRenamePreset(TaupeGun main, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, "Renommer le preset", "En développement.", "MENU_PRESET_RENAME",
                Material.NAME_TAG, preset, parent);
        this.getParent().reloadInventory();
    }

    @Override
    protected void action(Player player, StatsPlayerTaupe pl) {
        // TODO Rename the preset
    }

}
