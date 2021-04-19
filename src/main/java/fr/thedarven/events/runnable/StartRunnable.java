package fr.thedarven.events.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartRunnable extends BukkitRunnable {

    private TaupeGun main;

    public StartRunnable(TaupeGun main) {
        this.main = main;
    }

    @Override
    public void run() {
        String title = "";
        Sound sound = Sound.ORB_PICKUP;

        switch(this.main.getGameManager().getCooldownTimer()){
            case 10:
                title = ChatColor.DARK_RED + "10";
                break;
            case 5:
                title = ChatColor.RED + "5";
                break;
            case 4:
                title = ChatColor.YELLOW + "4";
                break;
            case 3:
                title = ChatColor.GOLD + "3";
                break;
            case 2:
                title = ChatColor.GREEN + "2";
                break;
            case 1:
                title = ChatColor.DARK_GREEN + "1";
                break;
            case 0:
                this.cancel();
                this.main.getGameManager().startGame();
                sound = Sound.ENDERDRAGON_GROWL;
                title = "§2" + LanguageBuilder.getContent("START_COMMAND", "gameIsStarting", true);
                break;
        }

        this.main.getPlayerManager().sendPlaySoundAndTitle(
                sound,
                new Title(title, "", 10)
        );

        this.main.getGameManager().decreaseCooldownTimer();
    }
}
