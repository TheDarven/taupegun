package fr.thedarven.events.event;

import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class TeamsInventoryClickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final StatsPlayerTaupe pl;
    private final Player player;
    private final ItemStack item;

    public TeamsInventoryClickEvent(StatsPlayerTaupe pl, Player player, ItemStack item) {
        this.pl = pl;
        this.player = player;
        this.item = item;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public StatsPlayerTaupe getPl() {
        return pl;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }
}
