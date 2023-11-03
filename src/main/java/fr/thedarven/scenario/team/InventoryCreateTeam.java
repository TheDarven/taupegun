package fr.thedarven.scenario.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.team.runnable.CreateTeamRunnable;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.api.anvil.AnvilGUI;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryCreateTeam extends ConfigurationInventory implements AdminConfiguration {

    private static String CREATE_TEAM = "Choix du nom";

    public InventoryCreateTeam(TaupeGun main, ConfigurationInventory parent) {
        super(main, "✚ Ajouter une équipe", null, "MENU_TEAM_ADD", 1, Material.BANNER,
                parent, 0, (byte) 15);
    }

    @Override
    public void loadLanguage(String language) {
        CREATE_TEAM = LanguageBuilder.getContent("TEAM", "nameChoice", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
        languageTeam.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "nameChoice", CREATE_TEAM);
        return languageElement;
    }

    @Override
    public void onClickIn(Player player, PlayerTaupe pl) {
        if (TeamCustom.countTeam() >= TeamCustom.MAX_TEAM_AMOUNT) {
            this.main.getMessageManager().sendTooManyTeamMessage(player);
            player.closeInventory();
            return;
        }

        addTeamAction(player, pl);
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
