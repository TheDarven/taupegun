package fr.thedarven.scenario.team.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builder.InventoryAction;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryTeamsChangeColor extends InventoryAction implements AdminConfiguration {

    public InventoryTeamsChangeColor(TaupeGun main, CustomInventory parent) {
        super(main, "Changer la couleur", "En développement.", "MENU_TEAM_ITEM_PARAMETER_COLOR",
                1, Material.BANNER, parent, 1, (byte) 15);
    }

    @Override
    protected void action(Player player, StatsPlayerTaupe pl) {
        // TODO Change color
    }

}
