package fr.thedarven.scenarios.teams.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;

import java.util.ArrayList;

public class InventoryTeamsParameters extends InventoryGUI implements AdminConfiguration {

protected static ArrayList<InventoryTeamsParameters> inventory = new ArrayList<>();

	public InventoryTeamsParameters(TaupeGun main, InventoryGUI parent) {
		super(main, "Paramètres", null, "MENU_TEAM_ITEM_PARAMETER", 1, Material.REDSTONE_COMPARATOR, parent, 22);
		inventory.add(this);
	}
	
}
