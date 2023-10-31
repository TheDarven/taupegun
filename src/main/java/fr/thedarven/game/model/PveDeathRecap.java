package fr.thedarven.game.model;

import fr.thedarven.player.model.PlayerTaupe;
import net.md_5.bungee.api.ChatColor;

public class PveDeathRecap implements GameRecap {

    private final PlayerTaupe victim;

    public PveDeathRecap(PlayerTaupe victim) {
        this.victim = victim;
    }

    @Override
    public String getMessage() {
        return String.format("%s%s%s (♰)", ChatColor.RED, victim.getName(), ChatColor.RESET);
    }
}
