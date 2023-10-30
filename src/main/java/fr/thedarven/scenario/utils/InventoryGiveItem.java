package fr.thedarven.scenario.utils;

import org.bukkit.entity.Player;

/**
 * Items donnant des items
 */
public interface InventoryGiveItem {

    /**
     * Pour donner les items à un joueur
     *
     * @param player Le joueur
     */
    void giveItems(Player player);

}
