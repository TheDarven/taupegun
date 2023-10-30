package fr.thedarven.game.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.utils.SortPlayerKill;
import fr.thedarven.utils.manager.MessageManager;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EndGameRunnable extends BukkitRunnable {

    private TaupeGun main;
    private int timer = 10;

    public EndGameRunnable(TaupeGun main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (StatsPlayerTaupe.getPlayerManager(player.getUniqueId()).isAlive()) {
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
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(killListMessage);
        List<StatsPlayerTaupe> kills = StatsPlayerTaupe.getAllPlayerManager().stream()
                .filter(pc -> pc.getKill() > 0)
                .sorted(new SortPlayerKill())
                .collect(Collectors.toList());
        kills.forEach(pc -> Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GREEN + pc.getName() + ": " + ChatColor.RESET + " " + pc.getKill()));

        String killPveMessage = "§2" + LanguageBuilder.getContent("CONTENT", "killPve", true);
        Map<String, String> params = new HashMap<>();
        params.put("amount", String.valueOf(this.main.getGameManager().getPveKills()));
        Bukkit.broadcastMessage(TextInterpreter.textInterpretation(killPveMessage, params));
    }
}
