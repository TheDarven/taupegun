package fr.thedarven.scenarios.players.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.players.InventoryPlayers;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryPlayersConfiguration extends InventoryPlayers implements AdminConfiguration {

    public InventoryPlayersConfiguration(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, int pPosition) {
        super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pPosition);
        loadPlayersConfiguration();
    }

    protected InventoryPlayersElement createElement(Player player) {
        return new InventoryPlayersElementConfiguration(this.main, this.name, this.getDescription(), this.getTranslationName(), 4, Material.DIRT, this, player.getUniqueId(), this);
    }

    private void loadPlayersConfiguration() {
        // save à la fermeture / load à l'ouverture
    }

    public final void savePlayersConfiguration() {

    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        openChildInventory(e.getCurrentItem(), player, pl);
    }

}
