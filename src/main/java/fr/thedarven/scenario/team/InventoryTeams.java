package fr.thedarven.scenario.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.builder.InventoryIncrement;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.scenario.team.element.InventoryTeamsElement;
import org.bukkit.Material;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryTeams extends InventoryIncrement implements AdminConfiguration {
	
	public InventoryTeams(TaupeGun main, CustomInventory parent) {
		super(main, "Équipes", "Menu des équipes.", "MENU_TEAM", 6, Material.BANNER, parent, 5, (byte) 15);
	}

	@Override
	public void reloadInventory() {
		clearChildrenItems();

		AtomicInteger pos = new AtomicInteger(0);
		getChildrenValue()
			.forEach(inv -> {
				if (inv instanceof InventoryTeamsRandom) {
					modifiyPosition(inv, inv.getPosition());
				} else if (inv instanceof InventoryTeamsElement) {
					modifiyPosition(inv, pos.getAndIncrement());
				} else {
					modifiyPosition(inv, getChildren().size() - 2);
				}
			});
	}

}
