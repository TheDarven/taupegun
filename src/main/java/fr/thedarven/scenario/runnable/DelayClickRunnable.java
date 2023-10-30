package fr.thedarven.scenario.runnable;

import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.scheduler.BukkitRunnable;

public class DelayClickRunnable extends BukkitRunnable {

    private final StatsPlayerTaupe playerTaupe;

    public DelayClickRunnable(StatsPlayerTaupe playerTaupe) {
        this.playerTaupe = playerTaupe;
    }

    @Override
    public void run() {
        this.playerTaupe.setCanClick(true);
        this.cancel();
    }
}
