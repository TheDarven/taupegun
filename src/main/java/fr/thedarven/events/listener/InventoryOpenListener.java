package fr.thedarven.events.listener;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Objects;

public class InventoryOpenListener implements Listener {

    private final TaupeGun main;

    public InventoryOpenListener(TaupeGun main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;

        CustomInventory openedInventory = CustomInventory.getCustomInventoryByInventory(event.getInventory());
        if (!Objects.isNull(openedInventory)) {
            openedInventory.onInventoryOpen(event);
            return;
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;

        CustomInventory closedInventory = CustomInventory.getCustomInventoryByInventory(event.getInventory());
        if (!Objects.isNull(closedInventory)) {
            closedInventory.onInventoryClose(event);
        }
    }

}
