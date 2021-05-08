package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.Preset;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class InventoryLoadPreset extends InventoryPresetAction implements AdminConfiguration {

    public InventoryLoadPreset(TaupeGun main, Preset preset, InventoryPlayersElementPreset parent) {
        super(main, preset.getName(), "Charger le preset.", "MENU_PRESET_ITEM", Material.DIRT, preset, parent);
        this.getParent().reloadInventory();
    }

    /**
     * Pour recharger les items dans l'inventaire
     */
    public void reloadItem(){
        ItemStack item = getItem();
        int hashCode = item.hashCode();
        ItemMeta itemM = item.getItemMeta();
        itemM.setDisplayName(this.preset.getName());
        item.setItemMeta(itemM);

        if (Objects.nonNull(this.getParent())) {
            this.getParent().updateChildItem(hashCode, item, this);
        }
    }

    @Override
    protected void action(Player player, PlayerTaupe pl) {
        // TODO Select the preset
    }
}
