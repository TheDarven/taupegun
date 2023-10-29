package fr.thedarven.scenario.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.helper.AdminConfiguration;
import fr.thedarven.scenario.players.InventoryPlayers;
import fr.thedarven.scenario.players.InventoryPlayersElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class InventoryPlayersPreset extends InventoryPlayers implements AdminConfiguration {

    public InventoryPlayersPreset(TaupeGun main, int pLines, Material pMaterial, InventoryGUI pParent, int pPosition) {
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
