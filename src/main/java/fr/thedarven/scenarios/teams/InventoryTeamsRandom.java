package fr.thedarven.scenarios.teams;

import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.Material;

public class InventoryTeamsRandom extends InventoryGUI {
	
	public InventoryTeamsRandom(InventoryTeams parent) {
		super("Equipes randoms", null, "MENU_TEAM_RANDOM", 1, Material.PAPER, parent, 45);
	}
}