package fr.thedarven.scenario.team.element.player;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.page.PageableInventory;
import fr.thedarven.team.model.TeamCustom;
import org.bukkit.Material;

public class PageableTeamsPlayersSelection extends PageableInventory<PlayerTaupe, TeamsPlayersSelectionPage> {

    private final TeamCustom team;

    public PageableTeamsPlayersSelection(TaupeGun main, ConfigurationInventory parent, TeamCustom team) {
        super(main, "Ajouter un joueur", null, "MENU_TEAM_ITEM_ADD_PLAYER", 6, Material.ARMOR_STAND, parent,
                0, (byte) 0, main.getListenerManager().getPlayersWithoutTeamPageData());
        this.team = team;
    }

    /**
     * @return Team to which the inventory is attached
     */
    protected TeamCustom getTeam() {
        return team;
    }

    @Override
    protected TeamsPlayersSelectionPage buildPage(int page) {
        TeamsPlayersSelectionPage builtPage = new TeamsPlayersSelectionPage(this.main, this.getLines(), this, page);
        builtPage.build();
        return builtPage;
    }

}
