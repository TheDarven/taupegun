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
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		int operation = 0;
		int number = 0;
		if (e.getWhoClicked() instanceof Player && Objects.nonNull(e.getClickedInventory()) && e.getClickedInventory().equals(this.inventory)) {
			Player player = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
			e.setCancelled(true);
			
			if (click(player,EnumConfiguration.OPTION) && e.getCurrentItem().getType() != Material.AIR && pl.getCanClick()) {
				if(isReturnItem(e.getCurrentItem(), e.getRawSlot())) {
					player.openInventory(this.getParent().getInventory());
					return;
				}
				
				if (e.getSlot() == 1 && this.morePas > 2) {
					operation = 1;
					number = this.pas * 100;
				} else if (e.getSlot() == 2 && this.morePas > 1) {
					operation = 1;
					number = this.pas * 10;
				} else if (e.getSlot() == 3) {
					operation = 1;
					number = this.pas;
				} else if (e.getSlot() == 5) {
					operation = 2;
					number = this.pas;
				} else if (e.getSlot() == 6 && this.morePas > 1) {
					operation = 2;
					number = this.pas * 10;
				} else if (e.getSlot() == 7 && this.morePas > 2) {
					operation = 2;
					number = this.pas * 100;
				}
				
				if (operation == 1) {
					if (this.min < this.value - number) {
						this.value = this.value - number;
					} else {
						this.value = this.min;
					}
					reloadItem();
				} else if (operation == 2) {
					if (this.max > this.value + number) {
						this.value = this.value + number;
					} else {
						this.value = this.max;
					}
					reloadItem();
				}
				
				World world = TaupeGun.getInstance().getWorldManager().getWorld();
				if (Objects.nonNull(world)) {
					world.getWorldBorder().setCenter(0, 0);
					world.getWorldBorder().setSize(getDiameter());
				}

				delayClick(pl);
			}
		}
	}
}
