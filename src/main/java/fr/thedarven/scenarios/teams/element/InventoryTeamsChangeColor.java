package fr.thedarven.scenarios.teams.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryAction;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryTeamsChangeColor extends InventoryAction implements AdminConfiguration {

    public InventoryTeamsChangeColor(TaupeGun main, InventoryGUI parent) {
        super(main, "Changer la couleur", "En développement.", "MENU_TEAM_ITEM_PARAMETER_COLOR",
                1, Material.BANNER, parent, 1, (byte) 15);
    }

    @Override
    protected void action(Player player, PlayerTaupe pl) {
        // TODO Change color
    }

}
