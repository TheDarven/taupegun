package fr.thedarven.players;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import fr.thedarven.utils.api.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerManager extends Manager {

    public PlayerManager(TaupeGun main) {
        super(main);
    }

    public void sendPlaySound(Sound sound) {
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), sound, 1, 1));
    }

    public void sendPlaySoundAndTitle(Sound sound, Title title) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1, 1);
            title.send(player);
        }
    }

    // this.main.getPlayerManager().sendPlaySound(Sound.ENDERDRAGON_DEATH);

}
