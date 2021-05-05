package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.Preset;
import fr.thedarven.scenarios.builders.InventoryAction;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class InventoryLoadPreset extends InventoryAction implements AdminConfiguration {

    private final Preset preset;

    public InventoryLoadPreset(TaupeGun main, Preset preset, InventoryPlayersElementPreset pParent) {
        super(main, preset.getName(), "Charger le preset", "MENU_PRESET_ITEM", 1, Material.DIRT, pParent);
        this.preset = preset;
        this.getParent().reloadInventory();
    }

    public Preset getPreset() {
        return this.preset;
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
        // TODO Selected preset
    }
}
