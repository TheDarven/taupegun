package fr.thedarven.events.runnable;

import fr.thedarven.scenario.team.element.InventoryTeamsPlayers;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseInventoryRunnable extends BukkitRunnable {

    @Override
    public void run() {
        InventoryTeamsPlayers.reloadInventories();
        this.cancel();
    }

}
