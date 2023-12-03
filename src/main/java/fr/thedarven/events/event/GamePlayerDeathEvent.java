package fr.thedarven.events.event;

import fr.thedarven.game.model.enums.GameDeathCause;
import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerTaupe playerTaupe;
    private final GameDeathCause cause;
    private String originalDeathMessage;

    public GamePlayerDeathEvent(PlayerTaupe playerTaupe, GameDeathCause cause) {
        this.playerTaupe = playerTaupe;
        this.cause = cause;
    }

    public GamePlayerDeathEvent(PlayerTaupe playerTaupe, GameDeathCause cause, String originalDeathMessage) {
        this.playerTaupe = playerTaupe;
        this.cause = cause;
        this.originalDeathMessage = originalDeathMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public PlayerTaupe getPlayerTaupe() {
        return playerTaupe;
    }

    public GameDeathCause getCause() {
        return cause;
    }

    public String getOriginalDeathMessage() {
        return originalDeathMessage;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
