package fr.thedarven.scenario.team.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.InventoryDelete;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryDeleteTeams extends InventoryDelete implements AdminConfiguration {

    private static String TEAM_DELETE_FORMAT = "L'équipe {teamName} a été supprimée avec succès.";

    public InventoryDeleteTeams(TaupeGun main, ConfigurationInventory parent) {
        super(main, parent, "Supprimer l'équipe", "MENU_TEAM_ITEM_DELETE", 18);
    }

    @Override
    public void loadLanguage(String language) {
        TEAM_DELETE_FORMAT = LanguageBuilder.getContent("TEAM", "delete", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
        languageTeam.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "delete", TEAM_DELETE_FORMAT);
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
        if (!this.main.getScenariosManager().teamsMenu.openInventory(player)) {
            player.closeInventory();
        }
        InventoryTeamsPlayers.reloadInventories();
    }
}
