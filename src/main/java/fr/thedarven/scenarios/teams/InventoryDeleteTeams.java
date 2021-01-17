package fr.thedarven.scenarios.teams;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryDelete;
import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.thedarven.models.TeamCustom;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryDeleteTeams extends InventoryDelete {
	
	private static String TEAM_DELETE_FORMAT = "L'équipe {teamName} a été supprimée avec succès.";
	
	public InventoryDeleteTeams(InventoryGUI pInventoryGUI) {
		super(pInventoryGUI, "Supprimer l'équipe", "MENU_TEAM_ITEM_DELETE", 18);
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
		params.put("teamName", "§e§l"+team.getName() + "§r§a");
		Title.sendActionBar(player, TextInterpreter.textInterpretation("§a" + TEAM_DELETE_FORMAT, params));
		
		TeamCustom teamDelete = TeamCustom.getTeamCustomByName(team.getName());
		if (Objects.nonNull(teamDelete)) {
			teamDelete.deleteTeam();
		}
		player.openInventory(TaupeGun.getInstance().getInventoryRegister().teamsMenu.getInventory());
		InventoryPlayers.reloadInventories();
	}
}
