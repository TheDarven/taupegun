package fr.thedarven.game.utils;

import fr.thedarven.player.model.StatsPlayerTaupe;

import java.util.Comparator;

public class SortPlayerKill implements Comparator<StatsPlayerTaupe> {

    @Override
    public int compare(StatsPlayerTaupe o1, StatsPlayerTaupe o2) {
        return o2.getKill() - o1.getKill();
    }

}
