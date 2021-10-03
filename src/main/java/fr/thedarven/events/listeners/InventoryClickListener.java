package fr.thedarven.events.listeners;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.events.TeamsInventoryClickEvent;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.models.enums.EnumInventory;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryClickListener implements Listener {

    private final TaupeGun main;

    public InventoryClickListener(TaupeGun pl) {
        this.main = pl;
    }

    @EventHandler
    public void clickInventory(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;

        InventoryGUI clickedInventory = InventoryGUI.getInventoryGUIByInventory(e.getInventory());
        if (!Objects.isNull(clickedInventory)) {
            e.setCancelled(true);
            clickedInventory.onInventoryPreClick(e);
            return;
        }

        Player player = (Player) e.getWhoClicked();
        PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
        Inventory clickInv = e.getClickedInventory();
        ItemStack clickItem = e.getCurrentItem();

        if (pl.getOpenedInventory().checkInventory(clickInv, EnumInventory.TEAM)) {
            TeamsInventoryClickEvent teamsInventoryClickListener = new TeamsInventoryClickEvent(pl, player, clickItem);
            Bukkit.getPluginManager().callEvent(teamsInventoryClickListener);
            e.setCancelled(true);
            return;
        }

        if (Objects.isNull(clickItem) || !EnumGameState.isCurrentState(EnumGameState.GAME))
            return;

        if (e.getCurrentItem().getType() == Material.GOLDEN_APPLE && e.getCurrentItem().getData().getData() == PlayerItemConsumeListener.NOTCH_APPLE_DATA) {
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 1.0f);
            ItemStack item = new ItemStack(Material.GOLDEN_APPLE, e.getCurrentItem().getAmount());
            e.setCurrentItem(item);
            return;
        }
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            Player player = e.getPlayer();
            PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
            ItemStack clickItem = player.getItemInHand();

            if (Objects.isNull(clickItem)) {
				return;
			}

            if (this.main.getScenariosManager().onPlayerItemClick(clickItem, pl)) {
                e.setCancelled(true);
                return;
            }

            if (clickItem.getType() == Material.PAPER && clickItem.getItemMeta().getDisplayName().equals("Crédits")) {
                if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
                    this.main.getScenariosManager().credits.openInventoryOfPlayer(player);
                }
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void inventoryDrag(InventoryDragEvent e) {
        if (!EnumGameState.isCurrentState(EnumGameState.LOBBY) || !(e.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) e.getWhoClicked();
        if (PlayerTaupe.getPlayerManager(player.getUniqueId()).getOpenedInventory().checkInventory(player.getOpenInventory().getTopInventory(), EnumInventory.TEAM)) {
            e.setCancelled(true);
        }
    }
}
