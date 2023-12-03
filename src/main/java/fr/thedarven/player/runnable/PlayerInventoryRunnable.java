package fr.thedarven.player.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.EnumPlayerInventoryType;
import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public abstract class PlayerInventoryRunnable extends PlayerRunnable {

    private final EnumPlayerInventoryType type;

    public PlayerInventoryRunnable(TaupeGun main, PlayerTaupe pl, EnumPlayerInventoryType type) {
        super(main, pl);
        this.type = type;
    }

    @Override
    protected void stopRunnable() {
        super.stopRunnable();

        Player player = this.pl.getPlayer();
        if (Objects.nonNull(player) && checkOpenedInventory(player)) {
            player.closeInventory();
        }
        this.pl.getOpenedInventory().setInventory(null, EnumPlayerInventoryType.NONE);
    }

    public void openInventory() {
        openInventory(pl.getPlayer());
    }

    protected void openInventory(Player player) {
        Inventory createdInventory = createInventory();
        if (Objects.isNull(createdInventory) || Objects.isNull(player)) {
            stopRunnable();
            return;
        }
        player.openInventory(createdInventory);
        this.pl.getOpenedInventory().setInventory(player.getOpenInventory().getTopInventory(), type);
    }

    protected abstract Inventory createInventory();

    protected boolean checkOpenedInventory(Player player) {
        return this.pl.getOpenedInventory().checkInventory(player.getOpenInventory().getTopInventory(), this.type);
    }

}
