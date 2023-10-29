package fr.thedarven.scenario.teams.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.helper.AdminConfiguration;
import org.bukkit.Material;

import java.util.ArrayList;

public class InventoryTeamsParameters extends InventoryGUI implements AdminConfiguration {

protected static ArrayList<InventoryTeamsParameters> inventory = new ArrayList<>();

	public InventoryTeamsParameters(TaupeGun main, InventoryGUI parent) {
		super(main, "Paramètres", null, "MENU_TEAM_ITEM_PARAMETER", 1, Material.REDSTONE_COMPARATOR, parent, 22);
		inventory.add(this);
	}
	
}
