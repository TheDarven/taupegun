package fr.thedarven.scenarios.teams;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.InventoryIncrement;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryTeams extends InventoryIncrement implements AdminConfiguration {
	
	public InventoryTeams(InventoryGUI parent) {
		super("Équipes", "Menu des équipes.", "MENU_TEAM", 6, Material.BANNER, parent, 5, (byte) 15);
	}

	@Override
	public void reloadInventory() {
		clearChildsItems();

		AtomicInteger pos = new AtomicInteger(0);
		getChildsValue()
			.forEach(inv -> {
				if (inv instanceof InventoryTeamsRandom) {
					modifiyPosition(inv, inv.getPosition());
				} else if (inv instanceof InventoryTeamsElement) {
					modifiyPosition(inv, pos.getAndIncrement());
				} else {
					modifiyPosition(inv,getChilds().size() - 2);
				}
			});
	}

}
