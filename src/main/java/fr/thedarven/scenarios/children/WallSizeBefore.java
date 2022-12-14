package fr.thedarven.scenarios.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.WallSizeHelper;
import fr.thedarven.scenarios.helper.NumericHelper;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Objects;

public class WallSizeBefore extends WallSizeHelper {

	public WallSizeBefore(TaupeGun main, InventoryGUI parent) {
		super(main, "Taille avant la réduction", "La taille du mur avant le début de la réduction.",
				"MENU_CONFIGURATION_WALL_BEFORE", Material.STONE, parent,
				new NumericHelper(500, 5000, 750, 50, 3, " blocs +/-", 1, false, 1));
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
