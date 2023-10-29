package fr.thedarven.events.runnable;

import fr.thedarven.scenario.teams.element.InventoryTeamsPlayers;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseInventoryRunnable extends BukkitRunnable {

    @Override
    public void run() {
        InventoryTeamsPlayers.reloadInventories();
        this.cancel();
    }

}
