package fr.thedarven.scenario.player.preset;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.preset.PresetCreateEvent;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.player.InventoryPlayers;
import fr.thedarven.scenario.player.InventoryPlayersElement;
import fr.thedarven.scenario.player.preset.model.PlayerConfiguration;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.UUID;

public class InventoryPlayersPreset extends InventoryPlayers implements AdminConfiguration {

    public InventoryPlayersPreset(TaupeGun main, int pLines, Material pMaterial, ConfigurationInventory pParent, int pPosition) {
        super(main, "Configurations sauvegardées", "Pour sauvegarder et charger ses configurations personnelles.",
                "MENU_PRESET", pLines, pMaterial, pParent, pPosition);
    }

    protected InventoryPlayersElement createElement(UUID uuid) {
        PlayerConfiguration playerConfiguration = this.main.getScenariosManager().getPlayerConfiguration(uuid);
        InventoryPlayersElementPreset element = new InventoryPlayersElementPreset(this.main, this.getLines(), Material.DIRT, this, uuid, this, playerConfiguration);
        element.build();

        new InventoryCreatePreset(this.main, element, playerConfiguration).build();

        playerConfiguration.getPresets().forEach(preset -> {
            PresetCreateEvent presetCreateEvent = new PresetCreateEvent(preset, playerConfiguration);
            Bukkit.getPluginManager().callEvent(presetCreateEvent);
        });

        return element;
    }

}
