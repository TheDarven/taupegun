package fr.thedarven.scenario.player.preset;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.player.InventoryPlayers;
import fr.thedarven.scenario.player.InventoryPlayersElement;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class InventoryPlayersPreset extends InventoryPlayers implements AdminConfiguration {

    public InventoryPlayersPreset(TaupeGun main, int pLines, Material pMaterial, CustomInventory pParent, int pPosition) {
        super(main, "Configurations sauvegardées", "Pour sauvegarder et charger ses configurations personnelles.",
                "MENU_PRESET", pLines, pMaterial, pParent, pPosition);
    }

    protected InventoryPlayersElement createElement(UUID uuid) {
        return new InventoryPlayersElementPreset(this.main, this.getLines(), Material.DIRT, this, uuid, this);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, StatsPlayerTaupe pl) {
        openChildInventory(e.getCurrentItem(), player, pl);
    }

}
