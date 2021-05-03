package fr.thedarven.scenarios.players;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InventoryPlayers extends InventoryGUI {

    protected final Map<UUID, InventoryPlayersElement> players = new HashMap<>();

    public InventoryPlayers(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, int pPosition) {
        super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pPosition);
    }

    protected abstract InventoryPlayersElement createElement(Player player);

    protected void addElement(InventoryPlayersElement element, UUID uuid) {
        players.put(uuid, element);
    }

    public void openInventoryOfPlayer(Player player) {
        player.openInventory(getInventoryOfPlayer(player).getInventory());
    }

    public InventoryPlayersElement getInventoryOfPlayer(Player player) {
        InventoryPlayersElement inventory;
        if (this.players.containsKey(player.getUniqueId())) {
            inventory = this.players.get(player.getUniqueId());
        } else {
            inventory = createElement(player);
        }
        return inventory;
    }
}
