package fr.thedarven.scenario.player;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public abstract class InventoryPlayersElement extends ConfigurationInventory {

    protected final UUID owner;
    protected final InventoryPlayers clusterParent;

    public InventoryPlayersElement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines,
                                   Material pMaterial, ConfigurationInventory pParent, UUID owner, InventoryPlayers clusterParent) {
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
        TreeInventory realParent = getParent().getParent();
        if (Objects.nonNull(realParent) && canPlayerOpenInventory(realParent, player)) {
            player.openInventory(realParent.getInventory());
        }
    }

    protected boolean canPlayerOpenInventory(TreeInventory treeInventory, Player player) {
        return super.canPlayerOpenInventory(treeInventory, player) && Objects.equals(player.getUniqueId(), this.owner);
    }

}
