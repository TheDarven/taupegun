package fr.thedarven.events.listener;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.api.DisableF3;
import fr.thedarven.utils.api.titles.Title;
import fr.thedarven.utils.helpers.DateHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class PlayerJoinQuitListener implements Listener {

    private final TaupeGun main;

    public PlayerJoinQuitListener(TaupeGun main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());

        e.setJoinMessage("§8(§a+§8) §7" + e.getPlayer().getName());

        if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
            if (!this.main.getScenariosManager().coordonneesVisibles.getValue()) {
                new DisableF3().disableF3(player);
            }

            if (!pl.isAlive() || Objects.isNull(pl.getTeam())) {
                player.setGameMode(GameMode.SPECTATOR);
                World world = this.main.getWorldManager().getWorld();
                if (Objects.nonNull(world)) {
                    player.teleport(new Location(world, 0, 200, 0));
                }
                this.main.getTeamManager().getSpectatorTeam().ifPresent(spectatorTeam -> spectatorTeam.joinTeam(pl));
                this.main.getPlayerManager().clearPlayer(player);
            }
        }
        loginAction(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage("§8(§c-§8) §7" + e.getPlayer().getName());

        if (EnumGameState.isCurrentState(EnumGameState.WAIT)) {
            this.main.getCommandManager().getStartCommand().stopStartRunnable();
            this.main.getGameManager().setCooldownTimer(10);

            EnumGameState.setState(EnumGameState.LOBBY);


            this.main.getPlayerManager().sendPlaySoundAndTitle(
                    Sound.WITHER_HURT,
                    new Title("§c" + LanguageBuilder.getContent("START_COMMAND", "gameStartingCancelled", true), "", 10)
            );


            for (PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
                pl.setTaupeTeam(null);
                pl.setSuperTaupeTeam(null);
            }

            this.main.getTeamManager().deleteMoleAndSuperMoleTeams();
        }
        leaveAction(player);
    }

    public void loginAction(Player player) {
        player.setScoreboard(TeamCustom.board);
        PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());

        this.main.getMessageManager().updateTabContent(player);
        this.main.getScoreboardManager().onLogin(player);
        this.main.getDatabaseManager().updatePlayerLast(player);

        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            this.main.getScenariosManager().reloadPlayerItemOfPlayer(player);
        }

        pl.setCustomName(player.getName());
        pl.setLastConnection();

        if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
            if (this.main.development) {
                new Title(ChatColor.GOLD + LanguageBuilder.getContent("EVENT_LOGIN", "developerModeTitle", true),
                        LanguageBuilder.getContent("EVENT_LOGIN", "developerModeSubtitle", true), 40)
                        .sendTitle(player);
            }

            World world = this.main.getWorldManager().getWorld();
            if (Objects.nonNull(world)) {
                player.teleport(new Location(world, 0.5, 201, 0.5));
            }
            player.setHealth(20);
            player.setLevel(0);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public void leaveAction(Player player) {
        this.main.getScoreboardManager().onLogout(player);
        this.main.getDatabaseManager().updatePlayerTimePlayed(player);
        this.main.getDatabaseManager().updatePlayerLast(player);
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            this.main.getScenariosManager().removePlayerItem(player);
        }

        PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
        pl.addTimePlayed((int) (DateHelper.getLongTimestamp() - pl.getLastConnection()));
    }
}
