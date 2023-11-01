package fr.thedarven.game.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.manager.MessageManager;
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
            timer--;
        }
    }

    private void endGameMessage() {
        MessageManager messageManager = this.main.getMessageManager();

        String moleListMessage = String.format("§l§6%s", LanguageBuilder.getContent("CONTENT", "moleList", true));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(moleListMessage);
        messageManager.sendTaupeListMessage(null);
        if (this.main.getScenariosManager().superMoles.getValue()) {
            messageManager.sendSuperTaupeListMessage(null);
        }
        messageManager.sendGameRecapMessage();
        messageManager.sendKillRankingMessage();
    }
}
