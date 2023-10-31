package fr.thedarven.game.model;

import fr.thedarven.player.model.PlayerTaupe;
import net.md_5.bungee.api.ChatColor;

public class ReviveRecap implements GameRecap {

    private final PlayerTaupe revivedPlayer;

    public ReviveRecap(PlayerTaupe revivedPlayer) {
        this.revivedPlayer = revivedPlayer;
    }

    @Override
    public String getMessage() {
        return String.format("%s%s%s (❤)", ChatColor.GREEN, revivedPlayer.getName(), ChatColor.RESET);
    }
}
