package fr.thedarven.events.command;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.stats.model.dto.GameDto;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.helpers.RandomHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;
import java.util.UUID;

public class InvisibleCommands implements Listener {

    private final TaupeGun main;

    public InvisibleCommands(TaupeGun main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerSendCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());

        String[] args = e.getMessage().split(" ");
        if (args.length == 0) {
            return;
        }

        if (args[0].equalsIgnoreCase("/timer")) {
            e.setCancelled(this.onTimerCommand(player, e.getMessage().split(" ")));
            return;
        }

        if (args[0].equalsIgnoreCase("/fstart")) {
            e.setCancelled(this.onFstartCommand(player, e.getMessage().split(" ")));
            return;
        }

        if (args[0].equalsIgnoreCase("/endgame")) {
            e.setCancelled(this.onEndgameCommand(player, e.getMessage().split(" ")));
            return;
        }

        if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
            if (args[0].equalsIgnoreCase("/me") && !pl.isAlive()) {
                e.setCancelled(true);
            } else if ((args[0].equalsIgnoreCase("/tell") || args[0].equalsIgnoreCase("/msg")) && !pl.isAlive()) {
                e.setCancelled(true);
                String cannotPrivateMessage = "§a[TaupeGun]§c " + LanguageBuilder.getContent("EVENT_TCHAT", "cannotPrivateMessage", true);
                player.sendMessage(cannotPrivateMessage);
            }
        }
    }

    private boolean onFstartCommand(Player player, String[] args) {
        if (!Objects.equals(player.getUniqueId(), UUID.fromString(GlobalVariable.THEDARVEN_UUID))) {
            return false;
        }

        if (this.main.getTeamManager().countTeams() == 0) {
            int i = 0;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                TeamCustom team = new TeamCustom(this.main, String.format("Team #%s", i++), ColorEnum.values()[RandomHelper.generate(ColorEnum.values().length)], 0, 0, false, true);
                team.joinTeam(PlayerTaupe.getPlayerManager(onlinePlayer.getUniqueId()));
            }
        }

        this.main.getGameManager().setCooldownTimer(1);
        player.performCommand("start");
        return true;
    }

    private boolean onTimerCommand(Player player, String[] args) {
        if (!Objects.equals(player.getUniqueId(), UUID.fromString(GlobalVariable.THEDARVEN_UUID)) || args.length < 2) {
            return false;
        }

        try {
            int newTimerValue = Integer.parseInt(args[1]);
            if (newTimerValue < 0) {
                throw new NumberFormatException();
            }
            this.main.getGameManager().setTimer(newTimerValue);
        } catch (NumberFormatException ignored) {
            player.sendMessage("§c" + LanguageBuilder.getContent("COMMAND", "invalidNumber", true));
        }
        return true;
    }

    private boolean onEndgameCommand(Player player, String[] args) {
        if (!Objects.equals(player.getUniqueId(), UUID.fromString(GlobalVariable.THEDARVEN_UUID))) {
            return false;
        }
        GameDto.endGames();
        return true;
    }

}
