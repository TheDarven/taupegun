package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.player.preset.utils.StorablePreset;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.scenario.utils.InventoryGiveItem;
import fr.thedarven.utils.helpers.ItemHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryStartItem extends ConfigurationInventory implements AdminConfiguration, InventoryGiveItem, StorablePreset {

    public InventoryStartItem(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Stuff de départ", "Configuration du stuff de départ.", "MENU_STARTER_KIT", 6, Material.CHEST, parent, 7);
    }

    @Override
    protected Inventory buildAndFillInventory() {
        Inventory inventory = super.buildAndFillInventory();

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta glassM = glass.getItemMeta();
        glassM.setDisplayName("§f");
        glass.setItemMeta(glassM);

        for (int i = 4; i < 9; i++) {
            inventory.setItem(i, glass);
        }

        for (int i = 45; i < 53; i++) {
            inventory.setItem(i, glass);
        }

        return inventory;
    }

    @Override
    public void giveItems(Player player) {
        Inventory currentInventory = this.getInventory();
        Inventory playerInventory = player.getInventory();

        for (int i = 0; i < 45; i++) {
            if (i < 4) {
                playerInventory.setItem(39 - i, currentInventory.getItem(i));
            } else if (i < 36) {
                playerInventory.setItem(i, currentInventory.getItem(i));
            } else {
                playerInventory.setItem(i - 36, currentInventory.getItem(i));
            }
        }
    }

    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        if (onChildClick(e.getCurrentItem(), player, pl)
                || isLockedCaseItem(e.getCurrentItem())) {
            return;
        }
        e.setCancelled(false);
    }

    @Override
    public Object getPresetValue() {
        Inventory currentInventory = this.getInventory();
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            ItemStack currentItem = currentInventory.getItem(i);
            if ((i > 9 || i < 4) && !ItemHelper.isNullOrAir(currentItem)) {
                items.add(ItemHelper.toBase64(currentInventory.getItem(i)));
            } else {
                items.add(null);
            }
        }
        return items;
    }

    @Override
    public void setPresetValue(Object value) {
        Inventory currentInventory = this.getInventory();
        if (!(value instanceof List)) {
            return;
        }

        for (int i = 0; i < 45; i++) {
            if (i < 4 || i > 8) {
                currentInventory.setItem(i, null);
            }
        }

        List<String> values = (ArrayList<String>) value;
        int index = 0;
        for (String item : values) {
            if (Objects.nonNull(item)) {
                currentInventory.setItem(index, ItemHelper.fromBase64(item));
            }
            index++;
        }
    }
}
