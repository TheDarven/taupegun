package fr.thedarven.scenarios.players.credits;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.DirectionEnum;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
import fr.thedarven.scenarios.runnable.SnakeRunnable;
import fr.thedarven.utils.ItemHelper;
import fr.thedarven.utils.api.skull.Skull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class InventoryCreditElement extends InventoryPlayersElement {

    private final static ItemStack UP_HEAD = Skull.getCustomSkull("https://textures.minecraft.net/texture/a99aaf2456a6122de8f6b62683f2bc2eed9abb81fd5bea1b4c23a58156b669");
    private final static ItemStack LEFT_HEAD = Skull.getCustomSkull("https://textures.minecraft.net/texture/5f133e91919db0acefdc272d67fd87b4be88dc44a958958824474e21e06d53e6");
    private final static ItemStack DOWN_HEAD = Skull.getCustomSkull("https://textures.minecraft.net/texture/3912d45b1c78cc22452723ee66ba2d15777cc288568d6c1b62a545b29c7187");
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

    public void endGameAndRemoveArrow(Player player) {
        removeDirectionalArrow(player);
        endGame();
    }

    private void giveDirectionalArrow(@Nullable Player player) {
        if (Objects.isNull(player)) {
            return;
        }

        Inventory playerInventory = player.getInventory();
        ItemMeta topHeadM = UP_HEAD.getItemMeta();
        topHeadM.setDisplayName("§2" + InventoryCredit.ARROW_UP);
        UP_HEAD.setItemMeta(topHeadM);
        playerInventory.setItem(13, UP_HEAD);

        ItemMeta leftHeadM = LEFT_HEAD.getItemMeta();
        leftHeadM.setDisplayName("§2" + InventoryCredit.ARROW_LEFT);
        LEFT_HEAD.setItemMeta(leftHeadM);
        playerInventory.setItem(21, LEFT_HEAD);

        ItemMeta bottomHeadM = DOWN_HEAD.getItemMeta();
        bottomHeadM.setDisplayName("§2" + InventoryCredit.ARROW_DOWN);
        DOWN_HEAD.setItemMeta(bottomHeadM);
        playerInventory.setItem(22, DOWN_HEAD);

        ItemMeta rightHeadM = RIGHT_HEAD.getItemMeta();
        rightHeadM.setDisplayName("§2" + InventoryCredit.ARROW_RIGHT);
        RIGHT_HEAD.setItemMeta(rightHeadM);
        playerInventory.setItem(23, RIGHT_HEAD);
    }

    private void removeDirectionalArrow(@Nullable Player player) {
        if (Objects.isNull(player)) {
            return;
        }

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
            if (!ItemHelper.isNullOrAir(item)) {
                player.sendMessage(InventoryCredit.NOT_ENOUGH_SLOT);
                player.closeInventory();
                return;
            }
        }

        giveDirectionalArrow(player);

        startGame();
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        endGameAndRemoveArrow((Player) event.getPlayer());
    }

    @Override
    public void onPlayerDisconnect(Player player) {
        endGameAndRemoveArrow(player);
    }


    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        if (Objects.isNull(clickedItem) || Objects.isNull(this.snakeRunnable)) {
            return;
        }

        if (clickedItem.hashCode() == UP_HEAD.hashCode()) {
            this.snakeRunnable.setDirection(DirectionEnum.TOP);
        } else if (clickedItem.hashCode() == DOWN_HEAD.hashCode()) {
            this.snakeRunnable.setDirection(DirectionEnum.BOTTOM);
        } else if (clickedItem.hashCode() == LEFT_HEAD.hashCode()) {
            this.snakeRunnable.setDirection(DirectionEnum.LEFT);
        } else if (clickedItem.hashCode() == RIGHT_HEAD.hashCode()) {
            this.snakeRunnable.setDirection(DirectionEnum.RIGHT);
        }
    }

}
