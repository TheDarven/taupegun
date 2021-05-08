package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.players.InventoryPlayers;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
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
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        openChildInventory(e.getCurrentItem(), player, pl);
    }

}
