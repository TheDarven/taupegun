package fr.thedarven.scenarios.players.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
import org.bukkit.Material;

import java.util.UUID;

public class InventoryPlayersElementConfiguration extends InventoryPlayersElement implements AdminConfiguration {

    public InventoryPlayersElementConfiguration(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, UUID owner, InventoryPlayersConfiguration clusterParent) {
        super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent, owner, clusterParent);
    }

}
