package fr.thedarven.scenario.players;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builders.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InventoryPlayers extends InventoryGUI {

    protected final Map<UUID, InventoryPlayersElement> players = new HashMap<>();

    public InventoryPlayers(TaupeGun main, String pName, String pDescription, String pTranslationName,
                            int pLines, Material pMaterial, InventoryGUI pParent, int pPosition) {
        super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pPosition);
    }

    protected abstract InventoryPlayersElement createElement(UUID uuid);

    protected void addElement(InventoryPlayersElement element, UUID uuid) {
        players.put(uuid, element);
    }

    public void openInventoryOfPlayer(Player player) {
        player.openInventory(getInventoryOfUuid(player.getUniqueId()).getInventory());
    }

    public InventoryPlayersElement getInventoryOfUuid(UUID uuid) {
        InventoryPlayersElement inventory;
        if (this.players.containsKey(uuid)) {
            inventory = this.players.get(uuid);
        } else {
            inventory = createElement(uuid);
        }
        return inventory;
    }
}
