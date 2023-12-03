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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InventoryDeleteTeams extends InventoryDelete implements AdminConfiguration {

    private static String TEAM_DELETE_FORMAT = "L'équipe {teamName} a été supprimée avec succès.";

    private final TeamCustom team;

    public InventoryDeleteTeams(TaupeGun main, ConfigurationInventory parent, TeamCustom team) {
        super(main, parent, "Supprimer l'équipe", "MENU_TEAM_ITEM_DELETE", 18);
        this.team = team;
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
        Map<String, String> params = new HashMap<>();
        params.put("teamName", String.format("§e§l%s§r§a", this.team.getName()));
        new ActionBar(TextInterpreter.textInterpretation(String.format("%s%s", ChatColor.GREEN, TEAM_DELETE_FORMAT), params)).sendActionBar(player);

        this.team.deleteTeam();
    }
}
