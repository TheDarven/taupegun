package fr.thedarven.scenarios.builders;

import fr.thedarven.models.PlayerTaupe;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class InventoryAction extends InventoryGUI {

    public InventoryAction(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, int pPosition, byte pData) {
        super(pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pPosition, pData);
    }

    public InventoryAction(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, byte pData) {
        super(pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pData);
    }

    public InventoryAction(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, int pPosition) {
        super(pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pPosition);
    }

    public InventoryAction(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent) {
        super(pName, pDescription, pTranslationName, pLines, pMaterial, pParent);
    }

    /**
     * L'action a réaliser lorsqu'on clic sur l'item de l'inventaire
     *
     * @param player Le Player qui clic sur l'item
     * @param pl Le PlayerTaupe du Player
     */
    abstract protected void action(Player player, PlayerTaupe pl);

}
