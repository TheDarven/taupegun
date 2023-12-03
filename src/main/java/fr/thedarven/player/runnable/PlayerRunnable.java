package fr.thedarven.player.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PlayerRunnable extends BukkitRunnable {

    protected final TaupeGun main;
    protected final PlayerTaupe pl;

    public PlayerRunnable(TaupeGun main, PlayerTaupe pl) {
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
