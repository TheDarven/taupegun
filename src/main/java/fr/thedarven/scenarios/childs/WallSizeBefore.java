package fr.thedarven.scenarios.childs;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.WallSizeHelper;
import fr.thedarven.scenarios.helper.NumericHelper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class WallSizeBefore extends WallSizeHelper {

	public WallSizeBefore(InventoryGUI parent) {
		super("Taille avant la réduction", "La taille du mur avant le début de la réduction.",
				"MENU_CONFIGURATION_WALL_BEFORE", Material.STONE, parent,
				new NumericHelper(500, 5000, 750, 50, 3, " blocs +/-", 1, false, 1));
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		super.onInventoryClick(e, player, pl);

		World world = TaupeGun.getInstance().getWorldManager().getWorld();
		if (Objects.nonNull(world)) {
			world.getWorldBorder().setCenter(0, 0);
			world.getWorldBorder().setSize(getDiameter());
		}
	}

}
