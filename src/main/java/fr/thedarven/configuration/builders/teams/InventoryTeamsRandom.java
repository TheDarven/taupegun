package fr.thedarven.configuration.builders.teams;

import fr.thedarven.TaupeGun;
import org.bukkit.Material;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;

public class InventoryTeamsRandom extends InventoryGUI {
	
	public InventoryTeamsRandom(InventoryTeams parent) {
		super("Equipes randoms", null, "MENU_TEAM_RANDOM", 1, Material.PAPER, parent, 45);
	}
}