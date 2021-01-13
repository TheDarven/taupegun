package fr.thedarven.models.runnable;

import fr.thedarven.models.PlayerTaupe;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PlayerRunnable extends BukkitRunnable {

    protected final PlayerTaupe pl;

    public PlayerRunnable(PlayerTaupe pl) {
        this.pl = pl;
        this.pl.addRunnable(this);
    }

    @Override
    public void run() {
        operate();
    }

    protected abstract void operate();

    protected void stopRunnable() {
        this.cancel();
        this.pl.removeRunnable(this.getClass());
    }

}
