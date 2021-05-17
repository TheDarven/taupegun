package fr.thedarven.game.utils;

import fr.thedarven.players.PlayerTaupe;

import java.util.Comparator;

public class SortPlayerKill implements Comparator<PlayerTaupe> {

    @Override
    public int compare(PlayerTaupe o1, PlayerTaupe o2) {
        return o2.getKill() - o1.getKill();
    }

}
