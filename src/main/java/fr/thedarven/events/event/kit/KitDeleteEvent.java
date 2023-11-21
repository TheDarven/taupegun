package fr.thedarven.events.event.kit;

import fr.thedarven.kit.model.Kit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Kit kit;

    public KitDeleteEvent(Kit kit) {
        this.kit = kit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Kit getKit() {
        return kit;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}