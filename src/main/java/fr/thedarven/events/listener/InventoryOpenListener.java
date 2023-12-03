package fr.thedarven.events.listener;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Optional;

public class InventoryOpenListener implements Listener {

    private final TaupeGun main;

    public InventoryOpenListener(TaupeGun main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;

        Optional<ConfigurationInventory> oOpenedInventory = ConfigurationInventory.getByInventory(event.getInventory());
        oOpenedInventory.ifPresent(openedInventory -> openedInventory.onInventoryOpen(event));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;

        Optional<ConfigurationInventory> oClosedInventory = ConfigurationInventory.getByInventory(event.getInventory());
        oClosedInventory.ifPresent(closedInventory -> closedInventory.onInventoryClose(event));
    }

}
