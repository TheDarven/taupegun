package fr.thedarven.scenario.team.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryTeamsRename extends ConfigurationInventory implements AdminConfiguration {

    public InventoryTeamsRename(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Changer le nom", "En développement.", "MENU_TEAM_ITEM_PARAMETER_RENAME",
                1, Material.PAPER, parent, 0);
    }

    @Override
    public void onClickIn(Player player, PlayerTaupe pl) {
        // TODO Change name
    }

}
