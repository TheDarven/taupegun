package fr.thedarven.scenario.teams;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.scenario.builders.InventoryAction;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.helper.AdminConfiguration;
import fr.thedarven.scenario.runnable.CreateTeamRunnable;
import fr.thedarven.utils.api.anvil.AnvilGUI;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryCreateTeam extends InventoryAction implements AdminConfiguration {

    private static String TOO_MANY_TEAM = "Vous ne pouvez pas créer plus de 36 équipes.";
    private static String CREATE_TEAM = "Choix du nom";

    public InventoryCreateTeam(TaupeGun main, InventoryGUI parent) {
        super(main, "✚ Ajouter une équipe", null, "MENU_TEAM_ADD", 1, Material.BANNER,
                parent, 0, (byte) 15);
    }

    @Override
    public void updateLanguage(String language) {
        CREATE_TEAM = LanguageBuilder.getContent("TEAM", "nameChoice", language, true);
        TOO_MANY_TEAM = LanguageBuilder.getContent("TEAM", "tooManyTeams", language, true);

        super.updateLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();

        LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
        languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameChoice", CREATE_TEAM);
        languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "tooManyTeams", TOO_MANY_TEAM);

        return languageElement;
    }

    @Override
    protected void action(Player player, StatsPlayerTaupe pl) {
        if (TeamCustom.board.getTeams().size() < 36) {
            addTeamAction(player, pl);
        } else {
            new ActionBar(ChatColor.RED + TOO_MANY_TEAM).sendActionBar(player);
            player.closeInventory();
        }
    }

    /**
     * Permet à un joueur de créer une équipe
     *
     * @param player Le Player qui souhaite créer une équipe
     * @param pl Le PlayerTaupe du joueur
     */
    private void addTeamAction(Player player, StatsPlayerTaupe pl) {
        new AnvilGUI(this.main, player, (menu, text) -> {
            pl.setCreateTeamName(text);
            new CreateTeamRunnable(this.main, pl, player).runTask(this.main);
            return true;
        }).setInputName(CREATE_TEAM).open();
    }

}
