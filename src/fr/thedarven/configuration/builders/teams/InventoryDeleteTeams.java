package fr.thedarven.configuration.builders.teams;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryDelete;
import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.TeamCustom;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryDeleteTeams extends InventoryDelete {
	
	private static String TEAM_DELETE_FORMAT = "L'équipe {teamName} a été supprimée avec succès.";
	
	public InventoryDeleteTeams(InventoryGUI pInventoryGUI) {
		super(pInventoryGUI, "Supprimer l'équipe", "MENU_TEAM_ITEM_DELETE", 18);
	}
	
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		TEAM_DELETE_FORMAT = LanguageBuilder.getContent("TEAM", "delete", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "delete", TEAM_DELETE_FORMAT);
		
		return languageElement;
	}
	
	
	
	
	
	
	/**
	 * Pour supprimer une équipe
	 * 
	 * @param p Le joueur qui a supprimé
	 */
	protected void deleteElement(Player p) {
		Team team = TeamCustom.board.getTeam(getParent().getName());
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("teamName", "§e§l"+team.getName()+"§r§a");
		Title.sendActionBar(p, TextInterpreter.textInterpretation("§a"+TEAM_DELETE_FORMAT, params));
		
		TeamCustom teamDelete = TeamCustom.getTeamCustom(team.getName());
		if(teamDelete != null)
			teamDelete.deleteTeam();
		p.openInventory(InventoryRegister.teams.getInventory());
		InventoryPlayers.reloadInventory();
	}
}
