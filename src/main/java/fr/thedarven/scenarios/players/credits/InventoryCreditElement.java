package fr.thedarven.scenarios.players.credits;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.DirectionEnum;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
import fr.thedarven.scenarios.runnable.SnakeRunnable;
import fr.thedarven.utils.api.skull.Skull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryCreditElement extends InventoryPlayersElement {

    private final static ItemStack TOP_HEAD = Skull.getCustomSkull("https://textures.minecraft.net/texture/a99aaf2456a6122de8f6b62683f2bc2eed9abb81fd5bea1b4c23a58156b669");
    private final static ItemStack LEFT_HEAD = Skull.getCustomSkull("https://textures.minecraft.net/texture/5f133e91919db0acefdc272d67fd87b4be88dc44a958958824474e21e06d53e6");
    private final static ItemStack BOTTOM_HEAD = Skull.getCustomSkull("https://textures.minecraft.net/texture/3912d45b1c78cc22452723ee66ba2d15777cc288568d6c1b62a545b29c7187");
    private final static ItemStack RIGHT_HEAD = Skull.getCustomSkull("https://textures.minecraft.net/texture/e3fc52264d8ad9e654f415bef01a23947edbccccf649373289bea4d149541f70");

    private SnakeRunnable snakeRunnable;

    public InventoryCreditElement(TaupeGun main, int pLines, Material pMaterial, InventoryGUI pParent, UUID owner, InventoryCredit clusterParent) {
        super(main, "Crédits", "Les crédits du plugin.", "MENU_CREDIT", pLines, pMaterial, pParent, owner, clusterParent);
        reloadItems();
    }

    public void startGame() {
        this.endGame();
        this.snakeRunnable = new SnakeRunnable(this);
        this.snakeRunnable.runTaskTimer(this.main, 5, 5);
    }

    public void endGame() {
        if (Objects.nonNull(this.snakeRunnable)) {
            try {
                this.snakeRunnable.cancel();
            } catch (IllegalStateException ignored) { }
        }
    }

    private void giveDirectionalArrow(Player player) {
        Inventory playerInventory = player.getInventory();
        playerInventory.setItem(13, TOP_HEAD);
        playerInventory.setItem(21, LEFT_HEAD);
        playerInventory.setItem(22, BOTTOM_HEAD);
        playerInventory.setItem(23, RIGHT_HEAD);
    }

    private void removeDirectionalArrow(Player player) {
        Inventory playerInventory = player.getInventory();
        playerInventory.setItem(13, null);
        playerInventory.setItem(21, null);
        playerInventory.setItem(22, null);
        playerInventory.setItem(23, null);
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory playerInventory = player.getInventory();

        int indexes[] = { 13, 21, 22, 23 };

        for (int index: indexes) {
            ItemStack item = playerInventory.getItem(index);
            if (Objects.nonNull(item) && item.getType() != Material.AIR) {
                // TODO Message vous n'avez pas la place dans votre inventaire
                player.closeInventory();
                return;
            }
        }

        giveDirectionalArrow(player);

        startGame();
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        removeDirectionalArrow((Player) event.getPlayer());
        endGame();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        if (Objects.isNull(clickedItem) || Objects.isNull(this.snakeRunnable)) {
            return;
        }

        if (clickedItem.hashCode() == TOP_HEAD.hashCode()) {
            this.snakeRunnable.setDirection(DirectionEnum.TOP);
        } else if (clickedItem.hashCode() == BOTTOM_HEAD.hashCode()) {
            this.snakeRunnable.setDirection(DirectionEnum.BOTTOM);
        } else if (clickedItem.hashCode() == LEFT_HEAD.hashCode()) {
            this.snakeRunnable.setDirection(DirectionEnum.LEFT);
        } else if (clickedItem.hashCode() == RIGHT_HEAD.hashCode()) {
            this.snakeRunnable.setDirection(DirectionEnum.RIGHT);
        }
    }

}
