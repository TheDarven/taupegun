package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.WallSizeHelper;
import fr.thedarven.scenario.utils.NumericParams;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Objects;

public class WallSizeBefore extends WallSizeHelper {

    public WallSizeBefore(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Taille avant la réduction", "La taille du mur avant le début de la réduction.",
                "MENU_CONFIGURATION_WALL_BEFORE", Material.STONE, parent,
                new NumericParams(500, 5000, 750, 50, 3, " blocs +/-", 1, false, 1));
    }

    public void setValue(int value) {
        super.setValue(value);
        World world = this.main.getWorldManager().getWorld();
        if (Objects.nonNull(world)) {
            world.getWorldBorder().setCenter(0, 0);
            world.getWorldBorder().setSize(getDiameter());
        }
    }

}
