package fr.thedarven.game.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.utils.messages.MessagesClass;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EndGameRunnable extends BukkitRunnable {

    private TaupeGun main;
    private int timer = 10;

    public EndGameRunnable(TaupeGun main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PlayerTaupe.getPlayerManager(player.getUniqueId()).isAlive()) {
                Firework f = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fM = f.getFireworkMeta();
                FireworkEffect effect = FireworkEffect.builder()
                        .flicker(true)
                        .withColor(Color.GREEN)
                        .withFade(Color.WHITE)
                        .with(FireworkEffect.Type.BALL)
                        .trail(true)
                        .build();

                fM.setPower(3);
                fM.addEffect(effect);
                f.setFireworkMeta(fM);
                f.detonate();
            }
        }
        if (timer == 0) {
            endGameMessage();
            this.cancel();
            EnumGameState.setState(EnumGameState.END);
            timer = 11;
        } else {
            timer --;
        }
    }

    private void endGameMessage() {
        MessagesClass.FinalTaupeAnnonceMessage();
        if (this.main.getInventoryRegister().superMoles.getValue()) {
            MessagesClass.FinalSuperTaupeAnnonceMessage();
        }
        MessagesClass.FinalKillAnnonceMessage();
    }
}
