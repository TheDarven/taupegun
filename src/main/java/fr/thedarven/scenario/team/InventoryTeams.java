package fr.thedarven.scenario.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.TeamCreateEvent;
import fr.thedarven.events.event.TeamDeleteEvent;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.InventoryIncrement;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.team.element.*;
import fr.thedarven.scenario.team.element.player.PageableTeamsPlayersSelection;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.model.TeamCustom;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class InventoryTeams extends InventoryIncrement implements AdminConfiguration {

    public InventoryTeams(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Équipes", "Menu des équipes.", "MENU_TEAM", 6, Material.BANNER, parent, 5, (byte) 15);
    }

    @EventHandler
    public void onTeamCreate(TeamCreateEvent event) {
        if (!EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            return;
        }
        InventoryTeamsElement teamInventory = new InventoryTeamsElement(this.main, this, event.getTeam());
        teamInventory.build();

        InventoryTeamsParameters parametersInventory = new InventoryTeamsParameters(this.main, teamInventory);
        parametersInventory.build();

        new InventoryTeamsChangeColor(this.main, parametersInventory).build();
        new InventoryTeamsRename(this.main, parametersInventory).build();
        new PageableTeamsPlayersSelection(this.main, teamInventory, event.getTeam()).build();
        new InventoryDeleteTeams(this.main, teamInventory, event.getTeam()).build();

        this.refreshInventoryItems();
    }

    @EventHandler
    public void onTeamDelete(TeamDeleteEvent event) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            this.refreshInventoryItems();
        }
    }

    @Override
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();
        removeChildrenItems();

        int position = 0;
        for (TreeInventory child: getChildren()) {
            if (child instanceof InventoryTeamsRandom) {
                updateChildPositionItem(child, child.getPosition());
            } else if (child instanceof InventoryTeamsElement) {
                updateChildPositionItem(child, position++);
            } else if (child instanceof InventoryCreateTeam && this.main.getTeamManager().countTeams() < TeamCustom.MAX_TEAM_AMOUNT) {
                updateChildPositionItem(child, countChildren() - 2);
            }
        }
    }

}
