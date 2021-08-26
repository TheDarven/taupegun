package fr.thedarven.scenarios.players.credits;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.players.InventoryPlayers;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
import org.bukkit.Material;

import java.util.UUID;

public class InventoryCredit extends InventoryPlayers {

    public InventoryCredit(TaupeGun main, Material pMaterial, InventoryGUI pParent, int pPosition) {
        super(main, "Crédits", "Les crédits du plugin.", "MENU_CREDIT", 6, pMaterial, pParent, pPosition);
    }

    @Override
    protected InventoryPlayersElement createElement(UUID uuid) {
        return new InventoryCreditElement(this.main, this.getLines(), Material.PAPER, this, uuid, this);
    }

}
