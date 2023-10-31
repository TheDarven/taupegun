package fr.thedarven.scenario.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.InventoryIncrement;
import fr.thedarven.scenario.team.element.InventoryTeamsElement;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryTeams extends InventoryIncrement implements AdminConfiguration {

    public InventoryTeams(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Équipes", "Menu des équipes.", "MENU_TEAM", 6, Material.BANNER, parent, 5, (byte) 15);
    }

    @Override
    public void reloadInventory() {
        removeChildrenItems();

        AtomicInteger pos = new AtomicInteger(0);
        getChildren()
                .forEach(inv -> {
                    if (inv instanceof InventoryTeamsRandom) {
                        updateChildPositionItem(inv, inv.getPosition());
                    } else if (inv instanceof InventoryTeamsElement) {
                        updateChildPositionItem(inv, pos.getAndIncrement());
                    } else {
                        updateChildPositionItem(inv, countChildren() - 2);
                    }
                });
    }

}
