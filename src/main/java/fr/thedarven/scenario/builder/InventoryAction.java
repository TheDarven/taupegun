package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class InventoryAction extends ConfigurationInventory {

    public InventoryAction(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, int position, byte data) {
        super(main, name, description, translationName, lines, material, parent, position, data);
    }

    public InventoryAction(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, int position) {
        this(main, name, description, translationName, lines, material, parent, position, (byte) 0);
    }

    public InventoryAction(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent) {
        this(main, name, description, translationName, lines, material, parent, 0, (byte) 0);
    }

    /**
     * L'action a réaliser lorsqu'on clic sur l'item de l'inventaire
     *
     * @param player Le Player qui clic sur l'item
     * @param pl     Le PlayerTaupe du Player
     */
    abstract protected void action(Player player, PlayerTaupe pl);

}
