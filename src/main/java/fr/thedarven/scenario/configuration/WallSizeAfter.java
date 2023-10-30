package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.builder.WallSizeHelper;
import fr.thedarven.scenario.utils.NumericHelper;
import org.bukkit.Material;

public class WallSizeAfter extends WallSizeHelper {

    public WallSizeAfter(TaupeGun main, CustomInventory parent) {
        super(main, "Taille après la réduction", "La taille du mur à la fin de la réduction.", "MENU_CONFIGURATION_WALL_AFTER",
                Material.BEDROCK, parent, new NumericHelper(25, 200, 100, 5, 2, " blocs +/-", 1, false, 1));
    }

}
