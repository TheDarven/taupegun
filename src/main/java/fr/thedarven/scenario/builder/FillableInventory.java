package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.helpers.ItemHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class FillableInventory extends ConfigurationInventory {

    public FillableInventory(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, int position) {
        super(main, name, description, translationName, lines, material, parent, position);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe playerTaupe) {
        if (onChildClick(e.getCurrentItem(), player, playerTaupe) || isLockedCaseItem(e.getCurrentItem())) {
            return;
        }
        e.setCancelled(false);
    }
    
}
