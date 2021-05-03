package fr.thedarven.scenarios.teams;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.scenarios.builders.InventoryAction;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.runnable.CreateTeamRunnable;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryCreateTeam extends InventoryAction implements  AdminConfiguration {

    private static String TOO_MUCH_TEAM = "Vous ne pouvez pas créer plus de 36 équipes.";
    private static String CREATE_TEAM = "Choix du nom";

    public InventoryCreateTeam(TaupeGun main, InventoryGUI parent) {
        super(main, "✚ Ajouter une équipe", null, "MENU_TEAM_ADD", 1, Material.BANNER,
                parent, 0, (byte) 15);
    }

    @Override
    public void updateLanguage(String language) {
        CREATE_TEAM = LanguageBuilder.getContent("TEAM", "nameChoice", language, true);
        TOO_MUCH_TEAM = LanguageBuilder.getContent("TEAM", "tooManyTeams", language, true);

        super.updateLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();

        LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
        languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameChoice", CREATE_TEAM);
        languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "tooManyTeams", TOO_MUCH_TEAM);

        return languageElement;
    }

    @Override
    protected void action(Player player, PlayerTaupe pl) {
        if (TeamCustom.board.getTeams().size() < 36) {
            addTeamAction(player, pl);
        } else {
            Title.sendActionBar(player, ChatColor.RED + TOO_MUCH_TEAM);
            player.closeInventory();
        }
    }

    /**
     * Permet à un joueur de créer une équipe
     *
     * @param player Le Player qui souhaite créer une équipe
     * @param pl Le PlayerTaupe du joueur
     */
    private void addTeamAction(Player player, PlayerTaupe pl) {
        new AnvilGUI(this.main, player, (menu, text) -> {
            pl.setCreateTeamName(text);
            new CreateTeamRunnable(this.main, pl, player).runTask(this.main);
            return true;
        }).setInputName(CREATE_TEAM).open();
    }

}
