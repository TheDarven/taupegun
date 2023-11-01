package fr.thedarven.game.model;

import fr.thedarven.player.model.PlayerTaupe;
import net.md_5.bungee.api.ChatColor;

public class KillRecap implements GameRecap {

    private final PlayerTaupe killer;
    private final PlayerTaupe victim;

    public KillRecap(PlayerTaupe killer, PlayerTaupe victim) {
        this.killer = killer;
        this.victim = victim;
    }

    @Override
    public String getMessage() {
        return String.format("%s ⚔ %s%s%s (♰)", killer.getName(), ChatColor.RED, victim.getName(), ChatColor.RESET);
    }
}
