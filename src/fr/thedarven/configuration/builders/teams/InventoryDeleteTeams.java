package fr.thedarven.configuration.builders.teams;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryDelete;
import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.utils.MessagesClass;

public class InventoryDeleteTeams extends InventoryDelete {
	
	public InventoryDeleteTeams(InventoryGUI pInventoryGUI) {
		super(pInventoryGUI, "Supprimer l'équipe", 18);
	}
	
	protected void deleteElement(Player p) {
		Team team = Teams.board.getTeam(getParent().getName());
		MessagesClass.TeamDeleteMessage(p, team.getName());
		Teams.deleteTeam(team.getName());
		p.openInventory(InventoryRegister.teams.getInventory());
	}
}
