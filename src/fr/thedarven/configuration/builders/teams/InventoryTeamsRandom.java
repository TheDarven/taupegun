package fr.thedarven.configuration.builders.teams;

import org.bukkit.Material;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;

public class InventoryTeamsRandom extends InventoryGUI {
	
	public InventoryTeamsRandom() {
		super("Equipes randoms", null, 1, Material.PAPER, InventoryRegister.teams, 45);
	}
}
