package fr.thedarven.scenario.utils;

import org.bukkit.entity.Player;

public interface ConfigurationPlayerItemConditional {

    /**
     * Informe si l'item de la configuration doit être donné au joueur.
     *
     * @param player Le joueur.
     * @return <b>true</b> si l'item doit être donné, <b>false</b> sinon.
     */
    boolean isPlayerItemEnable(Player player);

}
