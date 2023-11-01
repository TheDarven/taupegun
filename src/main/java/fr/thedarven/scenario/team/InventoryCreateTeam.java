package fr.thedarven.scenario.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.team.runnable.CreateTeamRunnable;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.api.anvil.AnvilGUI;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryCreateTeam extends ConfigurationInventory implements AdminConfiguration {

    private static String TOO_MANY_TEAM = "Vous ne pouvez pas créer plus de 36 équipes.";
    private static String CREATE_TEAM = "Choix du nom";

    public InventoryCreateTeam(TaupeGun main, ConfigurationInventory parent) {
        super(main, "✚ Ajouter une équipe", null, "MENU_TEAM_ADD", 1, Material.BANNER,
                parent, 0, (byte) 15);
    }

    @Override
    public void loadLanguage(String language) {
        CREATE_TEAM = LanguageBuilder.getContent("TEAM", "nameChoice", language, true);
        TOO_MANY_TEAM = LanguageBuilder.getContent("TEAM", "tooManyTeams", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
        languageTeam.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "nameChoice", CREATE_TEAM);
        languageTeam.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "tooManyTeams", TOO_MANY_TEAM);
        return languageElement;
    }

    @Override
    public void onClickIn(Player player, PlayerTaupe pl) {
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
     * @param pl     Le PlayerTaupe du joueur
     */
    private void addTeamAction(Player player, PlayerTaupe pl) {
        new AnvilGUI(this.main, player, (menu, text) -> {
            pl.setCreateTeamName(text);
            new CreateTeamRunnable(this.main, pl, player).runTask(this.main);
            return true;
        }).setInputName(CREATE_TEAM).open();
    }

}
