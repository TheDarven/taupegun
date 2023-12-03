package fr.thedarven.events.event.preset;

import fr.thedarven.scenario.player.preset.model.PlayerConfiguration;
import fr.thedarven.scenario.player.preset.model.Preset;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PresetCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Preset preset;
    private final PlayerConfiguration playerConfiguration;

    public PresetCreateEvent(Preset preset, PlayerConfiguration playerConfiguration) {
        this.preset = preset;
        this.playerConfiguration = playerConfiguration;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Preset getPreset() {
        return preset;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerConfiguration getPlayerConfiguration() {
        return playerConfiguration;
    }
}