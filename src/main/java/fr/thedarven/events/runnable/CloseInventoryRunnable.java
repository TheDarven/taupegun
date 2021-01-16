package fr.thedarven.events.runnable;

import fr.thedarven.scenarios.teams.InventoryPlayers;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseInventoryRunnable extends BukkitRunnable {

    @Override
    public void run() {
        InventoryPlayers.reloadInventories();
        this.cancel();
    }

}
