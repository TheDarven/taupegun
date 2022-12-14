package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.PlayerConfiguration;
import fr.thedarven.scenarios.Preset;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
import org.bukkit.Material;

import java.util.UUID;

public class InventoryPlayersElementPreset extends InventoryPlayersElement implements AdminConfiguration {

    private final PlayerConfiguration playerConfiguration;

    public InventoryPlayersElementPreset(TaupeGun main, int pLines, Material pMaterial, InventoryGUI pParent, UUID owner, InventoryPlayersPreset clusterParent) {
        super(main, "Configurations sauvegardées", "Pour sauvegarder et charger ses configurations personnelles.", "MENU_PRESET", pLines, pMaterial, pParent, owner, clusterParent);
        this.playerConfiguration = this.main.getScenariosManager().getPlayerConfiguration(this.owner);
        this.main.getScenariosManager().initInventoryOfPlayer(this.playerConfiguration);
        reloadInventory();
    }

    public void removePresetInventories(Preset preset) {
        getChildrenValue()
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
        clearChildrenItems();

        int nbPresets = this.playerConfiguration.getNbPresets();

        for (InventoryGUI inv : getChildrenValue()) {
            if (inv instanceof InventoryLoadPreset) {
                modifiyPosition(inv, ((InventoryLoadPreset) inv).getPreset().getIndex());
            } else if (inv instanceof InventoryRenamePreset) {
                modifiyPosition(inv, ((InventoryRenamePreset) inv).getPreset().getIndex() + PlayerConfiguration.NB_MAX_PRESETS * 2);
            } else if (inv instanceof InventoryUpdatePreset) {
                modifiyPosition(inv, ((InventoryUpdatePreset) inv).getPreset().getIndex() + PlayerConfiguration.NB_MAX_PRESETS);
            } else if (inv instanceof InventoryDeletePreset) {
                modifiyPosition(inv, ((InventoryDeletePreset) inv).getPreset().getIndex() + PlayerConfiguration.NB_MAX_PRESETS * 3);
            } else if (!this.playerConfiguration.isPresetAmountLimit()){
                modifiyPosition(inv,nbPresets);
            }
        }
    }


}
