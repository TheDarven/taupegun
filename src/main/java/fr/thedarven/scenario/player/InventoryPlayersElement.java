package fr.thedarven.scenario.player;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builder.CustomInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public abstract class InventoryPlayersElement extends CustomInventory {

    protected final UUID owner;
    protected final InventoryPlayers clusterParent;

    public InventoryPlayersElement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines,
                                   Material pMaterial, CustomInventory pParent, UUID owner, InventoryPlayers clusterParent) {
        super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent);
        this.owner = owner;
        this.clusterParent = clusterParent;
        this.clusterParent.addElement(this, owner);
    }

    public UUID getOwner() {
        return owner;
    }

    public StatsPlayerTaupe getPlayerTaupeOwner() {
        return StatsPlayerTaupe.getPlayerManager(this.owner);
    }

    public void onReturnClick(Player player) {
        if (Objects.isNull(getParent())) {
            return;
        }
        CustomInventory realParent = getParent().getParent();
        if (Objects.nonNull(realParent) && canOpenInventory(realParent, player)) {
            player.openInventory(realParent.getInventory());
        }
    }

    protected boolean canOpenInventory(CustomInventory customInventory, Player player) {
        return super.canOpenInventory(customInventory, player) && Objects.equals(player.getUniqueId(), this.owner);
    }

}
