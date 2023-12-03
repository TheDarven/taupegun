package fr.thedarven.scenario.runnable;

import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.scheduler.BukkitRunnable;

public class DelayClickRunnable extends BukkitRunnable {

    private final PlayerTaupe playerTaupe;

    public DelayClickRunnable(PlayerTaupe playerTaupe) {
        this.playerTaupe = playerTaupe;
    }

    @Override
    public void run() {
        this.playerTaupe.setCanClick(true);
        this.cancel();
    }
}
