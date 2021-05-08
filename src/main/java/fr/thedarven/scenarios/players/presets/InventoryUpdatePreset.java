package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.Preset;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryUpdatePreset extends InventoryPresetAction implements AdminConfiguration {

    public InventoryUpdatePreset(TaupeGun main, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, "Modifier le preset", "Permet de remplacer le preset sauvegardé par la configuration actuelle.",
                "MENU_PRESET_UPDATE", Material.ANVIL, preset, parent);
        this.getParent().reloadInventory();
    }

    @Override
    protected void action(Player player, PlayerTaupe pl) {
        // TODO Update the preset
    }

}
