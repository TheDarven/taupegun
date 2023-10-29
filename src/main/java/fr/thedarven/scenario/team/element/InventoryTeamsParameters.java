package fr.thedarven.scenario.team.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;

import java.util.ArrayList;

public class InventoryTeamsParameters extends CustomInventory implements AdminConfiguration {

protected static ArrayList<InventoryTeamsParameters> inventory = new ArrayList<>();

	public InventoryTeamsParameters(TaupeGun main, CustomInventory parent) {
		super(main, "Paramètres", null, "MENU_TEAM_ITEM_PARAMETER", 1, Material.REDSTONE_COMPARATOR, parent, 22);
		inventory.add(this);
	}
	
}
