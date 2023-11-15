package fr.thedarven.scenario.kit;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.kit.KitDeleteEvent;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.kit.model.Kit;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.FillableInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.scenario.utils.InventoryGiveItem;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.helpers.ItemHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InventoryKitsElement extends FillableInventory implements InventoryGiveItem, AdminConfiguration {

    protected final Kit kit;

    public InventoryKitsElement(TaupeGun main, InventoryKits parent, Kit kit) {
        super(main, kit.getName(), null, "MENU_KIT_ITEM", 2, Material.CHEST, parent, 0);
        this.kit = kit;
    }

    @Override
    protected String getNameOfLanguage(String language) {
        return kit.getName();
    }

    @Override
    protected String getDescriptionOfLanguage(String language) {
        return getDescription();
    }

    @Override
    protected String getInventoryName() {
        return this.getName();
    }

    public Kit getKit() {
        return kit;
    }

    @Override
    protected String getItemName() {
        String name = getName();
        if (Objects.nonNull(this.kit)) {
            name = this.kit.getName();
        }
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return TextInterpreter.textInterpretation(GlobalVariable.ELEMENT_ITEM_NAME_FORMAT, params);
    }

    @Override
    protected Inventory buildAndFillInventory() {
        Inventory inventory = super.buildAndFillInventory();

        ItemStack verre = ItemHelper.addTagOnItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15));
        ItemMeta verreM = verre.getItemMeta();
        verreM.setDisplayName("§f");
        verre.setItemMeta(verreM);

        for (int i = 10; i < 17; i++) {
            inventory.setItem(i, verre);
        }

        List<String> itemsString = this.kit.getItems();
        for (int i = 0; i < 9; i++) {
            String itemString = itemsString.get(i);
            if (Objects.nonNull(itemString)) {
                inventory.setItem(i, ItemHelper.fromBase64(itemString));
            } else {
                inventory.setItem(i, new ItemStack(Material.AIR));
            }
        }

        return inventory;
    }

    @Override
    public void giveItems(Player player) {
        Location playerLocation = player.getLocation();
        ItemStack item;

        for (int i = 0; i < 9; i++) {
            item = this.inventory.getItem(i);
            if (!ItemHelper.isNullOrAir(item)) {
                player.getWorld().dropItem(playerLocation, item);
            }
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe playerTaupe) {
        super.onInventoryClick(e, player, playerTaupe);
        if (e.isCancelled()) {
            return;
        }

        // Update the kit items
        List<String> items = kit.getItems();
        Inventory inventory = getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack item = inventory.getItem(i);
            if (!ItemHelper.isNullOrAir(item)) {
                items.set(i, ItemHelper.toBase64(item));
            } else {
                items.set(i, null);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onKitDelete(KitDeleteEvent event) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY) && event.getKit() == this.kit) {
            this.deleteInventory(true);
        }
    }

}
