package fr.thedarven.scenario.player.preset;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.player.InventoryPlayersElement;
import fr.thedarven.scenario.player.preset.model.PlayerConfiguration;
import fr.thedarven.scenario.player.preset.model.Preset;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;

import java.util.UUID;

public class InventoryPlayersElementPreset extends InventoryPlayersElement implements AdminConfiguration {

    private final PlayerConfiguration playerConfiguration;

    public InventoryPlayersElementPreset(TaupeGun main, int pLines, Material pMaterial, ConfigurationInventory pParent, UUID owner, InventoryPlayersPreset clusterParent) {
        super(main, "Configurations sauvegardées", "Pour sauvegarder et charger ses configurations personnelles.", "MENU_PRESET", pLines, pMaterial, pParent, owner, clusterParent);
        this.playerConfiguration = this.main.getScenariosManager().getPlayerConfiguration(this.owner);
    }

    @Override
    public TreeInventory build() {
        super.build();
        this.main.getScenariosManager().initInventoryOfPlayer(this.playerConfiguration);
        reloadInventory();
        return this;
    }

    public void removePresetInventories(Preset preset) {
        getChildren()
                .forEach(child -> {
                    if (child instanceof InventoryPresetAction) {
                        if (((InventoryPresetAction) child).getPreset() == preset) {
                            removeChild(child, false);
                        }
                    } else if (child instanceof InventoryDeletePreset) {
                        if (((InventoryDeletePreset) child).getPreset() == preset) {
                            removeChild(child, false);
                        }
                    }
                });
        reloadInventory();
    }

    @Override
    public void reloadInventory() {
        removeChildrenItems();

        int nbPresets = this.playerConfiguration.getNbPresets();

        for (TreeInventory treeInventory : getChildren()) {
            if (treeInventory instanceof InventoryLoadPreset) {
                updateChildPositionItem(treeInventory, ((InventoryLoadPreset) treeInventory).getPreset().getIndex());
            } else if (treeInventory instanceof InventoryRenamePreset) {
                updateChildPositionItem(treeInventory, ((InventoryRenamePreset) treeInventory).getPreset().getIndex() + PlayerConfiguration.NB_MAX_PRESETS * 2);
            } else if (treeInventory instanceof InventoryUpdatePreset) {
                updateChildPositionItem(treeInventory, ((InventoryUpdatePreset) treeInventory).getPreset().getIndex() + PlayerConfiguration.NB_MAX_PRESETS);
            } else if (treeInventory instanceof InventoryDeletePreset) {
                updateChildPositionItem(treeInventory, ((InventoryDeletePreset) treeInventory).getPreset().getIndex() + PlayerConfiguration.NB_MAX_PRESETS * 3);
            } else if (!this.playerConfiguration.isPresetAmountLimit()) {
                updateChildPositionItem(treeInventory, nbPresets);
            }
        }
    }


}
