package fr.thedarven.scenario.player.credit;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.player.InventoryPlayersElement;
import fr.thedarven.scenario.player.credit.model.enums.DirectionEnum;
import fr.thedarven.scenario.player.credit.runnable.SnakeRunnable;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.helpers.ItemHelper;
import org.bukkit.ChatColor;
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

    private final static ItemStack UP_HEAD = ItemHelper.addTagOnItemStack(Skull.getCustomSkull(Skull.UP_HEAD_URL));
    private final static ItemStack LEFT_HEAD = ItemHelper.addTagOnItemStack(Skull.getCustomSkull(Skull.LEFT_HEAD_URL));
    private final static ItemStack DOWN_HEAD = ItemHelper.addTagOnItemStack(Skull.getCustomSkull(Skull.DOWN_HEAD_URL));
    private final static ItemStack RIGHT_HEAD = ItemHelper.addTagOnItemStack(Skull.getCustomSkull(Skull.RIGHT_HEAD_URL));

    private SnakeRunnable snakeRunnable;

    public InventoryCreditElement(TaupeGun main, int pLines, Material pMaterial, ConfigurationInventory pParent, UUID owner, InventoryCredit clusterParent) {
        super(main, "Crédits", "Les crédits du plugin.", "MENU_CREDIT", pLines, pMaterial, pParent, owner, clusterParent);
    }

    @Override
    public TreeInventory build() {
        super.build();
        refreshInventoryItems();
        return this;
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
            } catch (IllegalStateException ignored) {
            }
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

        int[] indexes = {13, 21, 22, 23};

        for (int index : indexes) {
            ItemStack item = playerInventory.getItem(index);
            if (!ItemHelper.isNullOrAir(item)) {
                player.sendMessage(String.format("%s%s", ChatColor.RED, InventoryCredit.NOT_ENOUGH_SLOT));
                event.setCancelled(true);
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
