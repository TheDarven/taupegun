package fr.thedarven.events.event.team;

import fr.thedarven.team.model.TeamCustom;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final TeamCustom team;

    public TeamDeleteEvent(TeamCustom team) {
        this.team = team;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public TeamCustom getTeam() {
        return team;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}