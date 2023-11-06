package fr.thedarven.events.event.team;

import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinTeamEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerTaupe playerTaupe;
    private final TeamCustom team;

    public PlayerJoinTeamEvent(PlayerTaupe playerTaupe, TeamCustom team) {
        this.playerTaupe = playerTaupe;
        this.team = team;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public PlayerTaupe getPlayerTaupe() {
        return playerTaupe;
    }

    public TeamCustom getTeam() {
        return team;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
