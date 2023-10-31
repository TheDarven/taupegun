package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;

public abstract class InventoryIncrement extends ConfigurationInventory implements AdminConfiguration {

    public InventoryIncrement(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, int position, byte itemData) {
        super(main, name, description, translationName, lines, material, parent, position, itemData);
    }

    public InventoryIncrement(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, int position) {
        this(main, name, description, translationName, lines, material, parent, position, (byte) 0);
    }

    /**
     * Pour avoir le dernier enfant
     *
     * @return Le dernier enfant
     */
    final public TreeInventory getLastChild() {
        return getChildren().get(this.countChildren() - 1);
    }
}
