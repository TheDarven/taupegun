package fr.thedarven.scenarios.helper;

import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ConfigurationPlayerItem {

    private final InventoryGUI configuration;
    private final int position;
    private ItemStack item;

    public ConfigurationPlayerItem(InventoryGUI configuration, int position, ItemStack item) {
        this.configuration = configuration;
        this.position = position;
        this.setItem(item);
    }

    public ItemStack getItem() {
        return this.item;
    }

    private void setItem(ItemStack item) {
        this.item = this.configuration.getMain().getItemManager().addTagOnItemStack(item);
    }

    /**
     * Met à jour l'item de configuration des joueurs.
     *
     * @param item Le nouvel item
     */
    public final void reloadPlayersItem(ItemStack item) {
        final int exHashcode = this.item.hashCode();
        this.setItem(item);
        Bukkit.getOnlinePlayers().forEach(player -> reloadPlayerItem(player, exHashcode));
    }

    /**
     * Supprime et redonne l'item de configuration à un joueur
     *
     * @param player Le joueur
     */
    public final void reloadPlayerItem(Player player) {
        this.reloadPlayerItem(player, this.item.hashCode());
    }

    /**
     * Supprime et redonne l'item de configuration à un joueur
     *
     * @param player     Le joueur
     * @param exHashcode L'ancien hashcode de l'item
     */
    public final void reloadPlayerItem(Player player, int exHashcode) {
        removePlayerItem(player, exHashcode);
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            givePlayerItem(player);
        }
    }

    public void givePlayerItem(Player player) {
        if (!(this.configuration instanceof ConfigurationPlayerItemConditional) || ((ConfigurationPlayerItemConditional) this.configuration).isPlayerItemEnable()) {
            player.getInventory().setItem(this.position, this.item);
        }
    }

    /**
     * Supprime l'item de configuration à un joueur
     *
     * @param player Le joueur dont on doit supprimer le beacon
     */
    public final void removePlayerItem(Player player) {
        this.removePlayerItem(player, this.item.hashCode());
    }

    /**
     * Supprime l'item de configuration à un joueur
     *
     * @param player   Le joueur dont on doit supprimer le beacon
     * @param hashcode Le hashcode de l'item
     */
    public final void removePlayerItem(Player player, int hashcode) {
        Inventory playerInv = player.getInventory();

        for (int i = 0; i < playerInv.getSize(); i++) {
            ItemStack currentItem = playerInv.getItem(i);
            if (Objects.nonNull(currentItem) && currentItem.hashCode() == hashcode) {
                playerInv.setItem(i, new ItemStack(Material.AIR));
            }
        }
    }
}
