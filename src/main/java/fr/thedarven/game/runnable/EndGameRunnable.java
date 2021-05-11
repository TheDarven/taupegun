package fr.thedarven.game.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.messages.MessageManager;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        MessageManager messageManager = this.main.getMessageManager();

        messageManager.sendTaupeListMessage(null);
        if (this.main.getScenariosManager().superMoles.getValue()) {
            messageManager.sendSuperTaupeListMessage(null);
        }
        sendKillRankingMessage();
    }

    /**
     * Sends into server chat the number of kills of each player.
     */
    private void sendKillRankingMessage() {
        String killListMessage = "§l§6" + LanguageBuilder.getContent("CONTENT", "killList", true);
        Bukkit.broadcastMessage(killListMessage);
        PlayerTaupe.getAllPlayerManager().stream()
                .filter(pc -> pc.getKill() > 0)
                .forEach(pc -> Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GREEN + pc.getName() + ": " + ChatColor.RESET + " " + pc.getKill()));
    }
}
