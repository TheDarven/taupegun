package fr.thedarven.scenarios.childs;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.WallSizeHelper;
import fr.thedarven.scenarios.helper.NumericHelper;
import org.bukkit.Material;

public class WallSizeAfter extends WallSizeHelper {

    public WallSizeAfter(TaupeGun main, InventoryGUI parent) {
        super(main, "Taille après la réduction", "La taille du mur à la fin de la réduction.", "MENU_CONFIGURATION_WALL_AFTER",
                Material.BEDROCK, parent, new NumericHelper(25, 200, 100, 5, 2, " blocs +/-", 1, false, 1));
    }

}
