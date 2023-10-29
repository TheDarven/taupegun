package fr.thedarven.scenario.teams.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builders.InventoryAction;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryTeamsChangeColor extends InventoryAction implements AdminConfiguration {

    public InventoryTeamsChangeColor(TaupeGun main, InventoryGUI parent) {
        super(main, "Changer la couleur", "En développement.", "MENU_TEAM_ITEM_PARAMETER_COLOR",
                1, Material.BANNER, parent, 1, (byte) 15);
    }

    @Override
    protected void action(Player player, StatsPlayerTaupe pl) {
        // TODO Change color
    }

}
