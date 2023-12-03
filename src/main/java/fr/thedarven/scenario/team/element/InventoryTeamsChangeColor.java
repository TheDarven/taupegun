package fr.thedarven.scenario.team.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryTeamsChangeColor extends ConfigurationInventory implements AdminConfiguration {

    public InventoryTeamsChangeColor(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Changer la couleur", "En développement.", "MENU_TEAM_ITEM_PARAMETER_COLOR",
                1, Material.BANNER, parent, 1, (byte) 15);
    }

    @Override
    public void onClickIn(Player player, PlayerTaupe pl) {
        // TODO Change color
    }

}
