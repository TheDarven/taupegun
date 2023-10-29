package fr.thedarven.scenario.teams.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builders.InventoryAction;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryTeamsRename extends InventoryAction implements AdminConfiguration {

    public InventoryTeamsRename(TaupeGun main, InventoryGUI parent) {
        super(main, "Changer le nom", "En développement.", "MENU_TEAM_ITEM_PARAMETER_RENAME",
                1, Material.PAPER, parent, 0);
    }

    @Override
    protected void action(Player player, StatsPlayerTaupe pl) {
        // TODO Change name
    }

}
