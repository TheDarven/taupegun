package fr.thedarven.game.model;

import fr.thedarven.player.model.PlayerTaupe;
import net.md_5.bungee.api.ChatColor;

public class ReviveHistory implements GameHistory {

    private final PlayerTaupe revivedPlayer;

    public ReviveHistory(PlayerTaupe revivedPlayer) {
        this.revivedPlayer = revivedPlayer;
    }

    @Override
    public String getMessage() {
        return String.format("%s%s%s (❤)", ChatColor.GREEN, revivedPlayer.getName(), ChatColor.RESET);
    }
}
