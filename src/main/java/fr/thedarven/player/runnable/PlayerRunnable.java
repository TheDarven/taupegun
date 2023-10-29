package fr.thedarven.player.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PlayerRunnable extends BukkitRunnable {

    protected final TaupeGun main;
    protected final StatsPlayerTaupe pl;

    public PlayerRunnable(TaupeGun main, StatsPlayerTaupe pl) {
        this.main = main;
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
