package fr.thedarven.scenarios.players;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public abstract class InventoryPlayersElement extends InventoryGUI {

    protected final UUID owner;
    protected final InventoryPlayers clusterParent;

    public InventoryPlayersElement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines,
                                   Material pMaterial, InventoryGUI pParent, UUID owner, InventoryPlayers clusterParent) {
        super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent);
        this.owner = owner;
        this.clusterParent = clusterParent;
        this.clusterParent.addElement(this, owner);
    }

    public UUID getOwner() {
        return owner;
    }

    public PlayerTaupe getPlayerTaupeOwner() {
        return PlayerTaupe.getPlayerManager(this.owner);
    }

    public void onReturnClick(Player player) {
        if (Objects.isNull(getParent())) {
            return;
        }
        InventoryGUI realParent = getParent().getParent();
        if (Objects.nonNull(realParent) && canOpenInventory(realParent, player)) {
            player.openInventory(realParent.getInventory());
        }
    }

    protected boolean canOpenInventory(InventoryGUI inventoryGUI, Player player) {
        return super.canOpenInventory(inventoryGUI, player) && Objects.equals(player.getUniqueId(), this.owner);
    }

}
