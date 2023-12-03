package fr.thedarven.scenario.player.preset;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.preset.PresetCreateEvent;
import fr.thedarven.events.event.preset.PresetDeleteEvent;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.player.InventoryPlayersElement;
import fr.thedarven.scenario.player.preset.model.PlayerConfiguration;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.util.Objects;
import java.util.UUID;

public class InventoryPlayersElementPreset extends InventoryPlayersElement implements AdminConfiguration {

    private final PlayerConfiguration playerConfiguration;

    public InventoryPlayersElementPreset(TaupeGun main, int pLines, Material pMaterial, ConfigurationInventory pParent, UUID owner,
                                         InventoryPlayersPreset clusterParent, PlayerConfiguration playerConfiguration) {
        super(main, "Configurations sauvegardées", "Pour sauvegarder et charger ses configurations personnelles.",
                "MENU_PRESET", pLines, pMaterial, pParent, owner, clusterParent);
        this.playerConfiguration = playerConfiguration;
    }

    @Override
    public TreeInventory build() {
        super.build();
        return this;
    }

    @EventHandler
    public void onPresetCreate(PresetCreateEvent event) {
        if (event.getPlayerConfiguration() != this.playerConfiguration) {
            return;
        }

        new InventoryLoadPreset(this.main, event.getPreset(), this).build();
        new InventoryRenamePreset(this.main, event.getPreset(), this).build();
        new InventoryUpdatePreset(this.main, event.getPreset(), this).build();
        new InventoryDeletePreset(this.main, this, event.getPlayerConfiguration(), event.getPreset()).build();
        this.refreshInventoryItems();
    }

    @EventHandler
    public void onPresetDelete(PresetDeleteEvent event) {
        if (event.getPlayerConfiguration() != this.playerConfiguration) {
            return;
        }

        getChildren().stream()
                .filter(child -> child instanceof PresetInventory && ((PresetInventory) child).getPreset() == event.getPreset())
                .forEach(child -> child.deleteInventory(false));
        refreshInventoryItems();
    }

    @Override
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();
        if (Objects.isNull(inventory)) {
            return;
        }
        removeChildrenItems();

        int nbPresets = this.playerConfiguration.getNbPresets();
        for (TreeInventory treeInventory : getChildren()) {
            if (treeInventory instanceof InventoryLoadPreset) {
                updateChildPositionItem(treeInventory, ((InventoryLoadPreset) treeInventory).getPreset().getIndex());
            } else if (treeInventory instanceof InventoryRenamePreset) {
                updateChildPositionItem(treeInventory, ((InventoryRenamePreset) treeInventory).getPreset().getIndex() + 9 * 2);
            } else if (treeInventory instanceof InventoryUpdatePreset) {
                updateChildPositionItem(treeInventory, ((InventoryUpdatePreset) treeInventory).getPreset().getIndex() + 9);
            } else if (treeInventory instanceof InventoryDeletePreset) {
                updateChildPositionItem(treeInventory, ((InventoryDeletePreset) treeInventory).getPreset().getIndex() + 9 * 3);
            } else if (!this.playerConfiguration.isPresetAmountLimit()) {
                updateChildPositionItem(treeInventory, nbPresets);
            }
        }
    }


}
