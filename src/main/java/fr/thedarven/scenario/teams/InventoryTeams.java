package fr.thedarven.scenario.teams;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.builders.InventoryIncrement;
import fr.thedarven.scenario.helper.AdminConfiguration;
import fr.thedarven.scenario.teams.element.InventoryTeamsElement;
import org.bukkit.Material;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryTeams extends InventoryIncrement implements AdminConfiguration {
	
	public InventoryTeams(TaupeGun main, InventoryGUI parent) {
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
