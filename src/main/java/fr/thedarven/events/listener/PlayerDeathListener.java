package fr.thedarven.events.listener;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.GamePlayerDeathEvent;
import fr.thedarven.game.model.KillRecap;
import fr.thedarven.game.model.PlayerKillCommandDeathRecap;
import fr.thedarven.game.model.PveDeathRecap;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.game.model.enums.GameDeathCause;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.stats.model.dto.GameDto;
import fr.thedarven.stats.model.dto.PlayerDeathDto;
import fr.thedarven.stats.model.dto.PlayerKillDto;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerDeathListener implements Listener {

    private final TaupeGun main;

    public PlayerDeathListener(TaupeGun main) {
        this.main = main;
    }

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        PlayerTaupe plVictim = PlayerTaupe.getPlayerManager(victim.getUniqueId());

        String originalDeathMessage = e.getDeathMessage();
        e.setDeathMessage(null);

        GamePlayerDeathEvent gamePlayerDeathEvent = new GamePlayerDeathEvent(plVictim, plVictim.isPlayerKillCommand() ? GameDeathCause.PLAYER_KILL_COMMAND : GameDeathCause.PLAYER_DEATH_EVENT, originalDeathMessage);
        Bukkit.getPluginManager().callEvent(gamePlayerDeathEvent);

        plVictim.setPlayerKillCommand(false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGamePlayerDeath(GamePlayerDeathEvent event) {
        PlayerTaupe deadPlayerTaupe = event.getPlayerTaupe();
        Player deadPlayer = deadPlayerTaupe.getPlayer();

        this.main.getMessageManager().broadcastDeathMessage(deadPlayerTaupe);
        this.main.getPlayerManager().sendPlaySound(Sound.WITHER_SPAWN);

        // Remove the player from alive players
        if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
            deadPlayerTaupe.setAlive(false);
            dropItemsOnDeath(deadPlayer);

            if (deadPlayer != null) {
                deadPlayer.setGameMode(GameMode.SPECTATOR);
                World world = this.main.getWorldManager().getWorld();
                if (Objects.nonNull(world)) {
                    deadPlayer.teleport(new Location(world, 0, 200, 0));
                }
            }
        }

        // Join spectator team
        TeamCustom team = deadPlayerTaupe.getTeam();
        if (team != null) {
            team.leaveTeam(deadPlayerTaupe.getUuid());
        }
        this.main.getTeamManager().getSpectatorTeam().ifPresent(spectatorTeam -> spectatorTeam.joinTeam(deadPlayerTaupe));

        // Update stats, recap and database
        Player killer = event.getCause() != GameDeathCause.PLAYER_DEATH_EVENT && deadPlayer != null
                ? deadPlayer.getKiller()
                : null;
        updateDatabaseAndStatsAndRecap(deadPlayerTaupe, killer, event.getOriginalDeathMessage(), event.getCause());
    }

    private void updateDatabaseAndStatsAndRecap(PlayerTaupe deadPlayerTaupe, Player killer, String originalDeathMessage, GameDeathCause gameDeathCause) {
        if (!EnumGameState.isCurrentState(EnumGameState.GAME) || deadPlayerTaupe == null) {
            return;
        }

        this.main.getDatabaseManager().updateMoleDeath(deadPlayerTaupe.getUuid().toString(), 1);
        if (killer != null) {
            PlayerTaupe killerPlayerTaupe = PlayerTaupe.getPlayerManager(killer.getUniqueId());
            killerPlayerTaupe.setKill(killerPlayerTaupe.getKill() + 1);
            this.main.getGameManager().addToRecap(new KillRecap(killerPlayerTaupe, deadPlayerTaupe));
            this.main.getDatabaseManager().updateMoleKills(killer);
            if (killer.getGameMode() == GameMode.SURVIVAL) {
                GameDto.getCurrentGame().addPlayerKill(new PlayerKillDto(deadPlayerTaupe.getUuid(), killer.getUniqueId(), killer.getHealth()));
            }
        } else {
            if (gameDeathCause == GameDeathCause.PLAYER_KILL_COMMAND) {
                this.main.getGameManager().addToRecap(new PlayerKillCommandDeathRecap(deadPlayerTaupe));
            } else {
                this.main.getGameManager().addToRecap(new PveDeathRecap(deadPlayerTaupe, originalDeathMessage));
            }
        }

        // Create the player's death stats
        Player deadPlayer = deadPlayerTaupe.getPlayer();

        String playerDeathCause;
        switch (gameDeathCause) {
            case PLAYER_KILL_COMMAND:
                playerDeathCause = "PLAYER_KILL_COMMAND";
                break;
            case PLAYER_DEATH_EVENT:
            default:
                playerDeathCause = deadPlayer != null ? deadPlayer.getLastDamageCause().getCause().toString() : null;
        }

        String lastEntityTypeName = null;
        if (gameDeathCause == GameDeathCause.PLAYER_DEATH_EVENT
                && deadPlayer != null
                && deadPlayer.getLastDamageCause() != null
                && deadPlayer.getLastDamageCause().getEntityType() != null) {
            lastEntityTypeName = deadPlayer.getLastDamageCause().getEntityType().toString();
        }
        GameDto.getCurrentGame().addPlayerDeath(new PlayerDeathDto(deadPlayerTaupe.getUuid(), playerDeathCause, lastEntityTypeName));
    }

    private void dropItemsOnDeath(Player deadPlayer) {
        if (deadPlayer == null) {
            return;
        }

        if (this.main.getGameManager().areMolesRevealed()) {
            // Drop death items
            if (this.main.getScenariosManager().goldenHead.getValue() > 0) {
                // Drop player head
                Map<String, String> params = new HashMap<>();
                params.put("playerName", deadPlayer.getName());
                String headName = "§6" + TextInterpreter.textInterpretation(LanguageBuilder.getContent("ITEM", "head", true), params);
                deadPlayer.getWorld().dropItem(deadPlayer.getLocation(), ItemHelper.getPlayerHeadWithName(deadPlayer.getName(), headName));
            }
            int nbGoldenApple = this.main.getScenariosManager().deathGoldenApple.getIntValue();
            if (nbGoldenApple > 0) {
                deadPlayer.getWorld().dropItem(deadPlayer.getLocation(), new ItemStack(Material.GOLDEN_APPLE, nbGoldenApple));
            }
        }
    }
}
