package fr.thedarven.scenario.player;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;
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
        getRealParent().ifPresent(realParent -> realParent.openInventory(player));
    }

    /**
     * Retrieve the parent inventory of the PlayerInventory, which mean the parent of the parent.
     * This is because <b>this</b> is an instance of the PlayerInventory for a specific Player.
     * @return The real parent of the PlayerInventory.
     */
    public Optional<TreeInventory> getRealParent() {
        if (Objects.isNull(getParent())) {
            return Optional.empty();
        }
        return Optional.ofNullable(getParent().getParent());
    }

    protected boolean canPlayerOpenInventory(TreeInventory treeInventory, Player player) {
        return super.canPlayerOpenInventory(treeInventory, player) && Objects.equals(player.getUniqueId(), this.owner);
    }

}
