package fr.thedarven.utils.api.scoreboard;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScoreboardManager extends Manager {

    private final Map<UUID, PersonalScoreboard> scoreboards;

    private final ScheduledExecutorService executorMonoThread;

    public ScoreboardManager(TaupeGun main) {
        super(main);

        scoreboards = new HashMap<>();

        this.executorMonoThread = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            for (PersonalScoreboard scoreboard : scoreboards.values())
                this.executorMonoThread.execute(scoreboard::reloadData);
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

    public void onLogin(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            return;
        }
        scoreboards.put(player.getUniqueId(), new PersonalScoreboard(player, this.main));
    }

    public void onLogout(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).onLogout();
            scoreboards.remove(player.getUniqueId());
        }
    }

    public void update(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).reloadData();
        }
    }

}

