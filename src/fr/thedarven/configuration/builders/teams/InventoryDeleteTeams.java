package fr.thedarven.configuration.builders.teams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryDelete;
import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.TeamCustom;
import fr.thedarven.utils.api.Title;

public class InventoryDeleteTeams extends InventoryDelete {
	
	public InventoryDeleteTeams(InventoryGUI pInventoryGUI) {
		super(pInventoryGUI, "Supprimer l'équipe", 18);
	}
	
	protected void deleteElement(Player p) {
		Team team = TeamCustom.board.getTeam(getParent().getName());
		Title.sendActionBar(p, ChatColor.WHITE+" L'équipe "+ChatColor.YELLOW+ChatColor.BOLD+team.getName()+ChatColor.RESET+ChatColor.WHITE+" a été supprimée avec succès.");
		TeamCustom teamDelete = TeamCustom.getTeamCustom(team.getName());
		if(teamDelete != null)
			teamDelete.deleteTeam();
		p.openInventory(InventoryRegister.teams.getInventory());
	}
}
