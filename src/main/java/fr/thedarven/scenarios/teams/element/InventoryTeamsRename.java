package fr.thedarven.scenarios.teams.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryAction;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryTeamsRename extends InventoryAction implements AdminConfiguration {

    public InventoryTeamsRename(TaupeGun main, InventoryGUI parent) {
        super(main, "Changer le nom", "En développement.", "MENU_TEAM_ITEM_PARAMETER_RENAME",
                1, Material.PAPER, parent, 0);
    }

    @Override
    protected void action(Player player, PlayerTaupe pl) {
        // TODO Change name
    }

}
