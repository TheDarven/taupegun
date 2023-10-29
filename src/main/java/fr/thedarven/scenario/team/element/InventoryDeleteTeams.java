package fr.thedarven.scenario.team.element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.InventoryDelete;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.api.titles.ActionBar;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;

public class InventoryDeleteTeams extends InventoryDelete implements AdminConfiguration {
	
	private static String TEAM_DELETE_FORMAT = "L'équipe {teamName} a été supprimée avec succès.";
	
	public InventoryDeleteTeams(TaupeGun main, CustomInventory parent) {
		super(main, parent, "Supprimer l'équipe", "MENU_TEAM_ITEM_DELETE", 18);
	}





	@Override
	public void updateLanguage(String language) {
		TEAM_DELETE_FORMAT = LanguageBuilder.getContent("TEAM", "delete", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "delete", TEAM_DELETE_FORMAT);
		
		return languageElement;
	}
	
	
	
	
	@Override
	protected void deleteElement(Player player) {
		Team team = TeamCustom.board.getTeam(getParent().getName());
		
		Map<String, String> params = new HashMap<>();
		params.put("teamName", "§e§l" + team.getName() + "§r§a");
		new ActionBar(TextInterpreter.textInterpretation("§a" + TEAM_DELETE_FORMAT, params)).sendActionBar(player);
		
		TeamCustom teamDelete = TeamCustom.getTeamCustomByName(team.getName());
		if (Objects.nonNull(teamDelete)) {
			teamDelete.deleteTeam();
		}
		player.openInventory(this.main.getScenariosManager().teamsMenu.getInventory());
		InventoryTeamsPlayers.reloadInventories();
	}
}
