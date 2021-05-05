package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.PlayerConfiguration;
import fr.thedarven.scenarios.Preset;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
import fr.thedarven.scenarios.teams.InventoryTeamsPlayers;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Objects;
import java.util.UUID;

public class InventoryPlayersElementPreset extends InventoryPlayersElement implements AdminConfiguration {

    private final PlayerConfiguration playerConfiguration;

    public InventoryPlayersElementPreset(TaupeGun main, int pLines, Material pMaterial, InventoryGUI pParent, UUID owner, InventoryPlayersPreset clusterParent) {
        super(main, "Configurations sauvegardées", "Pour sauvegarder et charger ses configurations personnelles.", "MENU_PRESET", pLines, pMaterial, pParent, owner, clusterParent);
        this.playerConfiguration = this.main.getScenariosManager().getPlayerConfiguration(this.owner);
        createChildsInventory();
        reloadInventory();
    }

    private void createChildsInventory() {
        new InventoryCreatePreset(this.main, this, this.playerConfiguration);
        playerConfiguration.createInventoryOfPresets();
    }

    public void removePreset(Preset preset) {
        getChildsValue()
                .forEach(child -> {
                    if (child instanceof InventoryLoadPreset) {
                        if (((InventoryLoadPreset) child).getPreset() == preset) {
                            removeChild(child);
                        }
                    } else if (child instanceof InventoryDeletePreset) {
                        if (((InventoryDeletePreset) child).getPreset() == preset) {
                            removeChild(child);
                        }
                    }
                });
        reloadInventory();
    }

    @Override
    public void reloadInventory() {
        clearChildsItems();

        int nbPresets = this.playerConfiguration.getNbPresets();

        for (InventoryGUI inv : getChildsValue()) {
            if (inv instanceof InventoryLoadPreset) {
                modifiyPosition(inv, ((InventoryLoadPreset) inv).getPreset().getIndex());
            } else if (inv instanceof InventoryDeletePreset) {
                modifiyPosition(inv, ((InventoryDeletePreset) inv).getPreset().getIndex() + PlayerConfiguration.NB_MAX_PRESETS);
            } else if (this.playerConfiguration.isPresetAmountLimit()){
                modifiyPosition(inv,nbPresets);
            }
        }
    }


}
