package fr.thedarven.scenario.team.element.player;

import fr.thedarven.events.event.team.PlayerJoinTeamEvent;
import fr.thedarven.events.event.team.PlayerLeaveTeamEvent;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.page.PageData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayersWithoutTeamPageData extends PageData<PlayerTaupe> implements Listener {

    public PlayersWithoutTeamPageData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerTaupe playerTaupe = PlayerTaupe.getPlayerManager(player.getUniqueId());
            if (canBeAdd(playerTaupe)) {
                addElement(playerTaupe);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerTaupe playerTaupe = PlayerTaupe.getPlayerManager(event.getPlayer().getUniqueId());
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY) && playerTaupe.getTeam() != null) {
            removeElement(playerTaupe);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerTaupe playerTaupe = PlayerTaupe.getPlayerManager(event.getPlayer().getUniqueId());
        if (canBeAdd(playerTaupe)) {
            addElement(playerTaupe);
        }
    }

    @EventHandler
    public void onPlayerJoinTeam(PlayerJoinTeamEvent event) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY) && event.getPlayerTaupe().getTeam() != null) {
            removeElement(event.getPlayerTaupe());
        }
    }

    @EventHandler
    public void onPlayerLeaveTeam(PlayerLeaveTeamEvent event) {
        if (canBeAdd(event.getPlayerTaupe())) {
            addElement(event.getPlayerTaupe());
        }
    }

    /**
     * Check if a PlayerTaupe can be added as element
     *
     * @param playerTaupe The PlayerTaupe
     * @return <b>true</b> if it can, otherwise <b>false</b>
     */
    private boolean canBeAdd(PlayerTaupe playerTaupe) {
        return EnumGameState.isCurrentState(EnumGameState.LOBBY)
                && playerTaupe.getTeam() == null
                && playerTaupe.isOnline()
                && !containsElement(playerTaupe);
    }
}
